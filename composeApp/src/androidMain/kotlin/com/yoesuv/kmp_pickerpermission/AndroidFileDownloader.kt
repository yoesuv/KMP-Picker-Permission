package com.yoesuv.kmp_pickerpermission

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.client.statement.HttpResponse
import com.yoesuv.kmp_pickerpermission.download.FileDownloader
import com.yoesuv.kmp_pickerpermission.download.DownloadStatus
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.URLDecoder
import java.io.File

class AndroidFileDownloader(
    private val context: Context,
    private val httpClient: HttpClient = HttpClient(Android)
) : FileDownloader {

    override fun download(url: String): Flow<DownloadStatus> = flow {
        Log.d("AndroidFileDownloader", "Downloading url=$url")
        emit(DownloadStatus.Start)

        val fileName = deriveFileName(url)
        val mimeType = guessMimeType(fileName)

        val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            // On API < 29, Downloads collection URI is not available; use the general Files table
            MediaStore.Files.getContentUri("external")
        }

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.IS_PENDING, 1)
                // Optionally place under Downloads on Q+
                // put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            } else {
                // On API < 29, specify the absolute path to the public Downloads directory
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val targetFile = File(downloadsDir, fileName)
                put(MediaStore.MediaColumns.DATA, targetFile.absolutePath)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(collection, values)
            ?: throw IllegalStateException("Failed to create MediaStore item")

        try {
            resolver.openOutputStream(uri)?.use { out ->
                val response: HttpResponse = httpClient.get(url)
                if (!response.status.isSuccess()) {
                    throw IllegalStateException("HTTP ${response.status}")
                }
                val total: Long? = response.contentLength()
                var downloaded = 0L

                val channel = response.bodyAsChannel()
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (!channel.isClosedForRead) {
                    val read = channel.readAvailable(buffer, 0, buffer.size)
                    if (read <= 0) break
                    out.write(buffer, 0, read)
                    downloaded += read
                    emit(DownloadStatus.Progress(downloaded, total))
                }
                out.flush()
            } ?: throw IllegalStateException("Failed to open output stream")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val cv = ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 0)
                }
                resolver.update(uri, cv, null, null)
            }

            Log.d("AndroidFileDownloader", "Saved to Downloads as $fileName")
            emit(DownloadStatus.Success)
        } catch (t: Throwable) {
            Log.e("AndroidFileDownloader", "Download error", t)
            // Cleanup the failed file entry
            resolver.delete(uri, null, null)
            emit(DownloadStatus.Failed(t.message ?: "unknown"))
        }
    }.flowOn(Dispatchers.IO)

    private fun deriveFileName(url: String): String {
        return try {
            val decoded = URLDecoder.decode(url.substringAfterLast('/'), Charsets.UTF_8.name())
            decoded.ifBlank { DEFAULT_FILE_NAME }
        } catch (_: Throwable) {
            DEFAULT_FILE_NAME
        }
    }

    private fun guessMimeType(fileName: String): String {
        val lower = fileName.lowercase()
        return when {
            lower.endsWith(".pdf") -> "application/pdf"
            lower.endsWith(".jpg") || lower.endsWith(".jpeg") -> "image/jpeg"
            lower.endsWith(".png") -> "image/png"
            lower.endsWith(".txt") -> "text/plain"
            else -> "application/octet-stream"
        }
    }

    companion object {
        private const val DEFAULT_FILE_NAME = "downloaded_file"
        private const val DEFAULT_BUFFER_SIZE = 8192
    }
}

package com.yoesuv.kmp_pickerpermission

import com.yoesuv.kmp_pickerpermission.download.FileDownloader
import com.yoesuv.kmp_pickerpermission.download.DownloadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IOSFileDownloader : FileDownloader {
    override fun download(url: String): Flow<DownloadStatus> = flow {
        println("Downloading: $url")
        emit(DownloadStatus.Start)
        try {
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                val opened = withContext(Dispatchers.Main) {
                    val app = UIApplication.sharedApplication
                    // Check ability before attempting to open
                    if (!app.canOpenURL(nsUrl)) {
                        false
                    } else {
                        suspendCancellableCoroutine { cont ->
                            app.openURL(
                                nsUrl,
                                options = emptyMap<Any?, Any?>()
                            ) { success ->
                                cont.resume(success)
                            }
                        }
                    }
                }
                if (opened) {
                    emit(DownloadStatus.Success)
                } else {
                    emit(DownloadStatus.Failed("Unable to open URL"))
                }
            } else {
                emit(DownloadStatus.Failed("Invalid URL"))
            }
        } catch (t: Throwable) {
            emit(DownloadStatus.Failed(t.message ?: "unknown"))
        }
    }
}

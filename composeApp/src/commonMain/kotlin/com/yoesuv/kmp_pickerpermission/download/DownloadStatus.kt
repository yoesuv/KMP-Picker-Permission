package com.yoesuv.kmp_pickerpermission.download

sealed class DownloadStatus {
    data object Start : DownloadStatus()
    data class Progress(
        val bytesRead: Long,
        val contentLength: Long?
    ) : DownloadStatus() {
        val percent: Int? =
            contentLength?.takeIf { it > 0 }?.let { ((bytesRead * 100) / it).toInt() }
    }

    data object Success : DownloadStatus()
    data class Failed(val message: String) : DownloadStatus()
}

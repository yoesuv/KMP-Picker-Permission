package com.yoesuv.kmp_pickerpermission.download

import kotlinx.coroutines.flow.Flow

interface FileDownloader {
    fun download(url: String): Flow<DownloadStatus>
}

object DownloaderHolder {
    private var downloader: FileDownloader? = null

    fun provide(instance: FileDownloader) {
        downloader = instance
    }

    fun getOrNull(): FileDownloader? = downloader
}

package com.yoesuv.kmp_pickerpermission.download

interface FileDownloader {
    suspend fun download(url: String)
}

object DownloaderHolder {
    private var downloader: FileDownloader? = null

    fun provide(instance: FileDownloader) {
        downloader = instance
    }

    fun getOrNull(): FileDownloader? = downloader
}

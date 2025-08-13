package com.yoesuv.kmp_pickerpermission

import com.yoesuv.kmp_pickerpermission.download.FileDownloader

class IOSFileDownloader : FileDownloader {
    override suspend fun download(url: String) {
        println("IOSFileDownloader: Requested download url=" + url)
    }
}

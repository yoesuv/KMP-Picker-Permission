package com.yoesuv.kmp_pickerpermission

import android.util.Log
import com.yoesuv.kmp_pickerpermission.download.FileDownloader

class AndroidFileDownloader : FileDownloader {
    override suspend fun download(url: String) {
        Log.d("AndroidFileDownloader", "Requested download url=$url")
    }
}

package com.yoesuv.kmp_pickerpermission

import androidx.compose.ui.window.ComposeUIViewController
import com.yoesuv.kmp_pickerpermission.download.DownloaderHolder

fun MainViewController() = run {
    // Provide platform notifier to common code
    NotifierHolder.provide(IOSNotifier())
    // Provide platform downloader to common code
    DownloaderHolder.provide(IOSFileDownloader())
    ComposeUIViewController { App() }
}
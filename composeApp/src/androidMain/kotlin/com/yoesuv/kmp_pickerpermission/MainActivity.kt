package com.yoesuv.kmp_pickerpermission

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import com.yoesuv.kmp_pickerpermission.download.DownloaderHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        FileKit.init(this)

        // Provide platform notifier to common code
        NotifierHolder.provide(AndroidNotifier(this))

        // Provide platform downloader to common code
        DownloaderHolder.provide(AndroidFileDownloader())

        setContent {
            App()
        }
    }
}
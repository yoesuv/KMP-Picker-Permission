package com.yoesuv.kmp_pickerpermission.feature.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.yoesuv.kmp_pickerpermission.download.DownloaderHolder

class DownloadViewModel : ViewModel() {
    private val _status = MutableStateFlow("status download")
    val status: StateFlow<String> = _status.asStateFlow()

    fun onDownloadClicked() {
        val downloader = DownloaderHolder.getOrNull()
        if (downloader == null) {
            _status.value = "Downloader not provided"
            return
        }

        val url =
            "https://raw.githubusercontent.com/yoesuv/GetX-Playground/master/Sample%20Document%20pdf.pdf"

        _status.value = "Downloading..."
        viewModelScope.launch {
            try {
                downloader.download(url)
                _status.value = "Download triggered"
            } catch (e: Throwable) {
                _status.value = "Download error: ${e.message ?: "unknown"}"
            }
        }
    }
}

package com.yoesuv.kmp_pickerpermission.feature.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoesuv.kmp_pickerpermission.download.DownloaderHolder
import com.yoesuv.kmp_pickerpermission.download.DownloadStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DownloadViewModel : ViewModel() {
    private val _status = MutableStateFlow<DownloadStatus>(DownloadStatus.Start)
    val status: StateFlow<DownloadStatus> = _status.asStateFlow()

    fun onDownloadClicked() {
        val downloader = DownloaderHolder.getOrNull()
        if (downloader == null) {
            _status.value = DownloadStatus.Failed("Downloader not provided")
            return
        }

        val url =
            "https://raw.githubusercontent.com/yoesuv/GetX-Playground/master/Sample%20Document%20pdf.pdf"

        viewModelScope.launch {
            downloader
                .download(url)
                .collect { update -> _status.value = update }
        }
    }
}

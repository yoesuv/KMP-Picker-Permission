package com.yoesuv.kmp_pickerpermission.feature.download

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DownloadViewModel : ViewModel() {
    private val _status = MutableStateFlow("status download")
    val status: StateFlow<String> = _status.asStateFlow()

    fun onDownloadClicked() {
        // Will implement actual download later
        // Keep UI-only for now
    }
}

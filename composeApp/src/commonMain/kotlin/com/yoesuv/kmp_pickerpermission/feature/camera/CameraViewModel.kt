package com.yoesuv.kmp_pickerpermission.feature.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.openCameraPicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {

    private val _photoPath = MutableStateFlow<String?>(null)
    val photoPath: StateFlow<String?> = _photoPath.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun takePhoto() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val file = FileKit.openCameraPicker()
                _photoPath.value = file?.absolutePath()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

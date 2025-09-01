package com.yoesuv.kmp_pickerpermission.feature.camera

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object CameraViewModelFactory {
    fun create(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CameraViewModel()
            }
        }
    }
}

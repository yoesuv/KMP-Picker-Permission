package com.yoesuv.kmp_pickerpermission.feature.download

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object DownloadViewModelFactory {
    fun create(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                DownloadViewModel()
            }
        }
    }
}

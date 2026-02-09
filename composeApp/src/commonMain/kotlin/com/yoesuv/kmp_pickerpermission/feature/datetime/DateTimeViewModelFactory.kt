package com.yoesuv.kmp_pickerpermission.feature.datetime

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object DateTimeViewModelFactory {
    fun create(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                DateTimeViewModel()
            }
        }
    }
}

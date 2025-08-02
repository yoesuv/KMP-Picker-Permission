package com.yoesuv.kmp_pickerpermission.feature.location

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.icerock.moko.permissions.PermissionsController

object LocationViewModelFactory {
    fun create(permissionsController: PermissionsController): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                LocationViewModel(permissionsController)
            }
        }
    }
}

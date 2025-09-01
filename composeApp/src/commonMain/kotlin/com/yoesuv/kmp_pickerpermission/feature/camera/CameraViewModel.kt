package com.yoesuv.kmp_pickerpermission.feature.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.openCameraPicker
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.camera.CAMERA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val permissionsController: PermissionsController
) : ViewModel() {

    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    private val _photoPath = MutableStateFlow<String?>(null)
    val photoPath: StateFlow<String?> = _photoPath.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _permissionState.value = permissionsController.getPermissionState(Permission.CAMERA)
        }
    }

    fun requestCameraPermission(onGranted: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.CAMERA)
                _permissionState.value = PermissionState.Granted
                onGranted?.invoke()
            } catch (e: DeniedException) {
                _permissionState.value = PermissionState.Denied
            } catch (e: DeniedAlwaysException) {
                _permissionState.value = PermissionState.DeniedAlways
            } catch (e: RequestCanceledException) {
                // ignored
            }
        }
    }

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

    fun openAppSettings() {
        permissionsController.openAppSettings()
    }
}

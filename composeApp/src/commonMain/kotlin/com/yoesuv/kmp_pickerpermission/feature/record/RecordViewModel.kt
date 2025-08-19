package com.yoesuv.kmp_pickerpermission.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.microphone.RECORD_AUDIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordViewModel(
    private val permissionsController: PermissionsController
) : ViewModel() {

    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    init {
        viewModelScope.launch {
            _permissionState.value =
                permissionsController.getPermissionState(Permission.RECORD_AUDIO)
        }
    }

    fun requestMicrophonePermission(onGranted: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.RECORD_AUDIO)
                _permissionState.value = PermissionState.Granted
                onGranted?.invoke()
            } catch (e: DeniedException) {
                _permissionState.value = PermissionState.Denied
            } catch (e: DeniedAlwaysException) {
                _permissionState.value = PermissionState.DeniedAlways
            } catch (e: RequestCanceledException) {
                // ignored
            } finally {

            }
        }
    }

    fun openAppSettings() {
        permissionsController.openAppSettings()
    }
}

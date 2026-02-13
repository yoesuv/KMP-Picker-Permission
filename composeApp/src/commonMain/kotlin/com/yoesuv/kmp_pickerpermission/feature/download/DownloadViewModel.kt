package com.yoesuv.kmp_pickerpermission.feature.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoesuv.kmp_pickerpermission.download.DownloaderHolder
import com.yoesuv.kmp_pickerpermission.download.DownloadStatus
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.storage.WRITE_STORAGE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val permissionsController: PermissionsController
) : ViewModel() {
    private val _status = MutableStateFlow<DownloadStatus>(DownloadStatus.Start)
    val status: StateFlow<DownloadStatus> = _status.asStateFlow()

    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    init {
        viewModelScope.launch {
            _permissionState.value =
                permissionsController.getPermissionState(Permission.WRITE_STORAGE)
        }
    }

    fun requestStoragePermission(onGranted: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.WRITE_STORAGE)
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

    fun openAppSettings() {
        permissionsController.openAppSettings()
    }
}

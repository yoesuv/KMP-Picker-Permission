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
import dev.theolm.record.Record
import dev.theolm.record.config.OutputFormat
import dev.theolm.record.config.OutputLocation
import dev.theolm.record.config.RecordConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordViewModel(
    private val permissionsController: PermissionsController
) : ViewModel() {

    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _lastSavedPath = MutableStateFlow<String?>(null)
    val lastSavedPath: StateFlow<String?> = _lastSavedPath.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        Record.setConfig(RecordConfig(
            outputFormat = OutputFormat.WAV,
        ))
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

    fun startRecording() {
        viewModelScope.launch {
            runCatching {
                // Start recording with default configuration
                Record.startRecording()
            }.onSuccess {
                _isRecording.value = true
                _error.value = null
            }.onFailure { throwable ->
                _error.value = throwable.message ?: throwable.toString()
                _isRecording.value = false
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            runCatching {
                // Stop recording and get saved file path
                Record.stopRecording()
            }.onSuccess { savedPath ->
                _isRecording.value = false
                _lastSavedPath.value = savedPath
                _error.value = null
            }.onFailure { throwable ->
                _error.value = throwable.message ?: throwable.toString()
            }
        }
    }

    fun openAppSettings() {
        permissionsController.openAppSettings()
    }
}


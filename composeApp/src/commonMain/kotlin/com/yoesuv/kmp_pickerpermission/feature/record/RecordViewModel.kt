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
import dev.theolm.record.config.AudioEncoder
import dev.theolm.record.config.OutputFormat
import dev.theolm.record.config.RecordConfig
import eu.iamkonstantin.kotlin.gadulka.GadulkaPlayer
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.isRegularFile
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
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
        Record.setConfig(
            RecordConfig(
                outputFormat = OutputFormat.WAV,
                audioEncoder = AudioEncoder.PCM_16BIT
            )
        )
        viewModelScope.launch {
            _permissionState.value =
                permissionsController.getPermissionState(Permission.RECORD_AUDIO)
        }
        // Delete existing .wav files when ViewModel is created
        deleteWavFilesFromCache()
    }

    /**
     * Delete all .wav files from the cache directory
     */
    private fun deleteWavFilesFromCache() {
        viewModelScope.launch {
            try {
                val cacheDir = FileKit.cacheDir
                if (cacheDir.exists() && cacheDir.isDirectory()) {
                    val files = cacheDir.list()
                    files.forEach { file ->
                        if (file.isRegularFile() && file.name.endsWith(".wav", ignoreCase = true)) {
                            file.delete()
                            println("Deleted WAV file: ${file.name}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error deleting WAV files from cache: ${e.message}")
                _error.value = "Failed to clean cache: ${e.message}"
            }
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
                println("Saved recording path: $savedPath")
            }.onFailure { throwable ->
                _error.value = throwable.message ?: throwable.toString()
            }
        }
    }

    fun openAppSettings() {
        permissionsController.openAppSettings()
    }

    fun playAudio(player: GadulkaPlayer) {
        _lastSavedPath.value?.let { path ->
            try {
                // For local files, we need to convert the path to a proper URL format
                // If the path doesn't start with a scheme, we need to add "file://" prefix
                val url = if (path.startsWith("http") || path.startsWith("file://")) {
                    path
                } else {
                    "file://$path"
                }
                player.play(url)

            } catch (e: Exception) {
                _error.value = "Failed to play audio: ${e.message}"
            }
        } ?: run {
            _error.value = "No recorded audio found"
        }
    }

    fun stopAudio(player: GadulkaPlayer) {
        player.stop()
        player.release()
    }

    /**
     * Delete .wav files when ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        deleteWavFilesFromCache()
    }
}

package com.yoesuv.kmp_pickerpermission.feature.notification

import com.yoesuv.kmp_pickerpermission.isNotificationPermissionRequired
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import com.yoesuv.kmp_pickerpermission.NotifierHolder

data class NotificationState(
    val status: String = "Unknown"
)

class NotificationViewModel(
    private val permissionsController: PermissionsController
) : ViewModel() {
    
    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()
    
    private val _notificationState = MutableStateFlow(NotificationState())
    val notificationState: StateFlow<NotificationState> = _notificationState.asStateFlow()
    
    init {
        viewModelScope.launch {
            // Initialize with current permission state
            _permissionState.value = permissionsController.getPermissionState(Permission.REMOTE_NOTIFICATION)
            
            // Update notification status based on current permission state
            if (_permissionState.value == PermissionState.Granted) {
                updateNotificationStatus()
            }
        }
    }
    
    fun requestNotificationPermission() {
        viewModelScope.launch {
            try {
                // Only request permission if required by the platform
                if (isNotificationPermissionRequired()) {
                    permissionsController.providePermission(Permission.REMOTE_NOTIFICATION)
                }
                _permissionState.value = PermissionState.Granted
                updateNotificationStatus()
                // Option B: Automatically show a local notification after permission is granted
                showLocalNotification()
            } catch (e: DeniedException) {
                _permissionState.value = PermissionState.Denied
            } catch (e: DeniedAlwaysException) {
                _permissionState.value = PermissionState.DeniedAlways
            } catch (e: RequestCanceledException) {
                // User cancelled the permission request
                e.printStackTrace()
            }
        }
    }
    
    private fun updateNotificationStatus() {
        // This is a placeholder. On Android, we can't directly check notification settings from common code.
        // On iOS, you would check the actual notification settings here.
        _notificationState.value = _notificationState.value.copy(
            status = "Granted"
        )
    }
    
    fun showLocalNotification() {
        NotifierHolder.getOrNull()?.showNotification(
            title = "KMP Local Notification",
            message = "Hello from KMP!"
        )
    }

    fun openAppSettings() {
        permissionsController.openAppSettings()
    }
}

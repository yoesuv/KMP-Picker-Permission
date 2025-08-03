package com.yoesuv.kmp_pickerpermission

import android.os.Build

actual fun getCurrentPlatform(): Platform = Platform.ANDROID

/**
 * On Android, notification permission is only required for API 33 (Android 13) and above
 */
actual fun isNotificationPermissionRequired(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}

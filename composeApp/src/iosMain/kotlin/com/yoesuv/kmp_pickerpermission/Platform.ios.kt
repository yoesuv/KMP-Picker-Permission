package com.yoesuv.kmp_pickerpermission

actual fun getCurrentPlatform(): Platform = Platform.IOS

/**
 * On iOS, notification permission is always required
 */
actual fun isNotificationPermissionRequired(): Boolean = true

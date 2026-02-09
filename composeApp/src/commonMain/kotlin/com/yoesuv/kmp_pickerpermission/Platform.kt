package com.yoesuv.kmp_pickerpermission

enum class Platform {
    ANDROID, IOS
}

expect fun getCurrentPlatform(): Platform

/**
 * Checks if the current Android version requires notification permission (API 33+)
 * Returns false on non-Android platforms or Android versions below 13 (API 33)
 */
expect fun isNotificationPermissionRequired(): Boolean

/**
 * Checks if the current platform requires WRITE_EXTERNAL_STORAGE permission for downloads.
 * Returns true only on Android versions below 10 (API < 29).
 * Returns false on Android 10+ (scoped storage) and iOS.
 */
expect fun isStoragePermissionRequired(): Boolean

val Platform.isAndroid: Boolean
    get() = this == Platform.ANDROID

val Platform.isIOS: Boolean
    get() = this == Platform.IOS
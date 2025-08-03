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

val Platform.isAndroid: Boolean
    get() = this == Platform.ANDROID

val Platform.isIOS: Boolean
    get() = this == Platform.IOS
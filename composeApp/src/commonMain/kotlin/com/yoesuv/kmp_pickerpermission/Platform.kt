package com.yoesuv.kmp_pickerpermission

enum class Platform {
    ANDROID, IOS
}

expect fun getCurrentPlatform(): Platform

val Platform.isAndroid: Boolean
    get() = this == Platform.ANDROID

val Platform.isIOS: Boolean
    get() = this == Platform.IOS
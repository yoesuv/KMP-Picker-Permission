package com.yoesuv.kmp_pickerpermission

enum class Platform {
    ANDROID, IOS
}

expect fun getCurrentPlatform(): Platform
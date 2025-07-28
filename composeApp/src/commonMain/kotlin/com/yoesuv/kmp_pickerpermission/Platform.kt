package com.yoesuv.kmp_pickerpermission

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
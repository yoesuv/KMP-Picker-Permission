package com.yoesuv.kmp_pickerpermission.core.route

import kotlinx.serialization.Serializable

sealed class AppRoute {
    @Serializable object Home : AppRoute()
    @Serializable object Gallery : AppRoute()
    @Serializable object Camera : AppRoute()
    @Serializable object File : AppRoute()
    @Serializable object DateTime : AppRoute()
    @Serializable object Location : AppRoute()
}
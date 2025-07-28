package com.yoesuv.kmp_pickerpermission

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yoesuv.kmp_pickerpermission.core.route.AppRoute
import com.yoesuv.kmp_pickerpermission.feature.gallery.GalleryScreen
import com.yoesuv.kmp_pickerpermission.feature.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = AppRoute.Home) {
            composable<AppRoute.Home> {
                HomeScreen(navController)
            }
            composable<AppRoute.Gallery> {
                GalleryScreen(navController)
            }
        }
    }

}
package com.yoesuv.kmp_pickerpermission

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yoesuv.kmp_pickerpermission.core.route.AppRoute
import com.yoesuv.kmp_pickerpermission.feature.camera.CameraScreen
import com.yoesuv.kmp_pickerpermission.feature.datetime.DateTimeScreen
import com.yoesuv.kmp_pickerpermission.feature.file.FileScreen
import com.yoesuv.kmp_pickerpermission.feature.gallery.GalleryScreen
import com.yoesuv.kmp_pickerpermission.feature.home.HomeScreen
import com.yoesuv.kmp_pickerpermission.feature.location.LocationScreen
import com.yoesuv.kmp_pickerpermission.feature.notification.NotificationScreen
import com.yoesuv.kmp_pickerpermission.feature.download.DownloadScreen
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
            composable<AppRoute.Camera> {
                CameraScreen(navController)
            }
            composable<AppRoute.File> {
                FileScreen(navController)
            }
            composable<AppRoute.DateTime> {
                DateTimeScreen(navController)
            }
            composable<AppRoute.Location> {
                LocationScreen(navController)
            }
            composable<AppRoute.Notification> {
                NotificationScreen(navController)
            }
            composable<AppRoute.Download> {
                DownloadScreen(navController)
            }
        }
    }
}
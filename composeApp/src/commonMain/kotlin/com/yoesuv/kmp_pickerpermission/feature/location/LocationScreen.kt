package com.yoesuv.kmp_pickerpermission.feature.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.get_location
import kmppickerpermission.composeapp.generated.resources.location
import kmppickerpermission.composeapp.generated.resources.open_settings
import androidx.compose.runtime.collectAsState
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocationScreen(nav: NavHostController) {
    val permissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember(permissionsControllerFactory) {
        permissionsControllerFactory.createPermissionsController()
    }

    BindEffect(permissionsController)

    val viewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory.create(permissionsController)
    )

    val permissionState by viewModel.permissionState.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.location),
                canBack = true,
                navigateUp = {
                    nav.navigateUp()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (permissionState) {
                PermissionState.NotDetermined -> {
                    Text(
                        text = "Latitude, Longitude",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                PermissionState.Granted -> {
                    currentLocation?.let { location ->
                        Text(
                            text = "${location.latitude ?: 0}, ${location.longitude ?: 0}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } ?: Text(
                        text = "Latitude, Longitude",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                PermissionState.Denied -> {
                    Text(
                        text = "Latitude, Longitude",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                PermissionState.DeniedAlways -> {
                    Text(
                        text = "Latitude, Longitude",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                else -> {
                    Text(
                        text = "Latitude, Longitude",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (permissionState) {
                PermissionState.NotDetermined -> {
                    // Initial state - show nothing
                }
                PermissionState.Granted -> {
                    currentLocation?.let { location ->
                        Text(
                            text = "Location permission granted.",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                PermissionState.Denied -> {
                    Text(
                        text = "Location permission denied.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                PermissionState.DeniedAlways -> {
                    Text(
                        text = "Location permission permanently denied. Please enable it in settings.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                else -> {
                    Text(
                        text = "Location permission not granted.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Single button that conditionally opens settings or requests permission
            AppButton(
                text = if (permissionState == PermissionState.DeniedAlways) {
                    stringResource(Res.string.open_settings)
                } else {
                    stringResource(Res.string.get_location)
                },
                onClick = {
                    if (permissionState == PermissionState.DeniedAlways) {
                        viewModel.openAppSettings()
                    } else {
                        viewModel.requestLocationPermission()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

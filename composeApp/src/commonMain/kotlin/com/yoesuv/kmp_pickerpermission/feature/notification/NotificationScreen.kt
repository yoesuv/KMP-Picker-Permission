package com.yoesuv.kmp_pickerpermission.feature.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import kmppickerpermission.composeapp.generated.resources.notification_status
import kmppickerpermission.composeapp.generated.resources.local_notification
import kmppickerpermission.composeapp.generated.resources.notifications
import kmppickerpermission.composeapp.generated.resources.open_settings
import kmppickerpermission.composeapp.generated.resources.notification_permission_granted
import kmppickerpermission.composeapp.generated.resources.notification_permission_denied
import kmppickerpermission.composeapp.generated.resources.notification_permission_permanently_denied
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationScreen(nav: NavHostController) {
    val permissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember(permissionsControllerFactory) {
        permissionsControllerFactory.createPermissionsController()
    }

    BindEffect(permissionsController)

    val viewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory.create(permissionsController)
    )

    val permissionState by viewModel.permissionState.collectAsState()
    val notificationState by viewModel.notificationState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.notifications),
                canBack = true,
                navigateUp = { nav.navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Display notification status
            Text(
                text = stringResource(Res.string.notification_status),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = notificationState.status,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            // Display permission status
            when (permissionState) {
                PermissionState.Granted -> {
                    Text(
                        text = stringResource(Res.string.notification_permission_granted),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                PermissionState.Denied -> {
                    Text(
                        text = stringResource(Res.string.notification_permission_denied),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                PermissionState.DeniedAlways -> {
                    Text(
                        text = stringResource(Res.string.notification_permission_permanently_denied),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Request permission button
            AppButton(
                text = if (permissionState == PermissionState.DeniedAlways ||
                    permissionState == PermissionState.Denied
                ) {
                    stringResource(Res.string.open_settings)
                } else {
                    stringResource(Res.string.local_notification)
                },
                onClick = {
                    if (permissionState == PermissionState.DeniedAlways ||
                        permissionState == PermissionState.Denied
                    ) {
                        viewModel.openAppSettings()
                    } else {
                        viewModel.requestNotificationPermission()
                    }
                },
                loading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

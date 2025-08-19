package com.yoesuv.kmp_pickerpermission.feature.record

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.is_recording
import kmppickerpermission.composeapp.generated.resources.pause
import kmppickerpermission.composeapp.generated.resources.record_audio
import kmppickerpermission.composeapp.generated.resources.start
import kmppickerpermission.composeapp.generated.resources.stop
import kmppickerpermission.composeapp.generated.resources.timer_default
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecordScreen(nav: NavHostController) {
    var status by remember { mutableStateOf(RecordingStatus.Stop) }

    // Permissions controller & VM like LocationScreen
    val permissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember(permissionsControllerFactory) {
        permissionsControllerFactory.createPermissionsController()
    }

    BindEffect(permissionsController)

    val viewModel: RecordViewModel = viewModel(
        factory = RecordViewModelFactory.create(permissionsController)
    )

    val permissionState by viewModel.permissionState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.record_audio),
                canBack = true,
                navigateUp = { nav.navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val statusLabel = when (status) {
                RecordingStatus.Start -> stringResource(Res.string.start)
                RecordingStatus.Pause -> stringResource(Res.string.pause)
                RecordingStatus.Stop -> stringResource(Res.string.stop)
            }
            // Status text
            Text(
                text = stringResource(Res.string.is_recording, statusLabel),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Timer display (static as requested)
            Text(
                text = stringResource(Res.string.timer_default),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Start/Pause button
            AppButton(
                text = if (status == RecordingStatus.Start) stringResource(Res.string.pause) else stringResource(
                    Res.string.start
                ),
                onClick = {
                    if (status == RecordingStatus.Start) {
                        // Pause doesn't need mic permission
                        status = RecordingStatus.Pause
                    } else {
                        // Starting recording: ensure mic permission
                        when (permissionState) {
                            PermissionState.Granted -> {
                                status = RecordingStatus.Start
                            }

                            PermissionState.Denied, PermissionState.DeniedAlways -> {
                                viewModel.openAppSettings()
                            }

                            else -> {
                                viewModel.requestMicrophonePermission {
                                    status = RecordingStatus.Start
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Stop button
            AppButton(
                text = stringResource(Res.string.stop),
                onClick = { status = RecordingStatus.Stop },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private enum class RecordingStatus {
    Start,
    Pause,
    Stop
}

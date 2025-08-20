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
import kmppickerpermission.composeapp.generated.resources.record_audio
import kmppickerpermission.composeapp.generated.resources.start
import kmppickerpermission.composeapp.generated.resources.stop
import org.jetbrains.compose.resources.stringResource
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun RecordScreen(nav: NavHostController) {
    var status by remember { mutableStateOf(RecordingStatus.Stop) }
    var counter by remember { mutableStateOf(0) }

    // Timer formatting function
    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "${minutes.toString().padStart(2, '0')}:${
            remainingSeconds.toString().padStart(2, '0')
        }"
    }

    // Timer effect
    LaunchedEffect(status) {
        if (status == RecordingStatus.Start) {
            // Reset counter only when starting
            counter = 0
            while (status == RecordingStatus.Start) {
                delay(1000) // Update every second
                counter++
            }
        }
        // When stopping, we don't reset the counter
        // It will retain its value until a new start
    }

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
                RecordingStatus.Stop -> stringResource(Res.string.stop)
            }
            // Status text
            Text(
                text = stringResource(Res.string.is_recording, statusLabel),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Timer display
            Text(
                text = formatTime(counter),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Start/Stop button
            AppButton(
                text = if (status == RecordingStatus.Start) stringResource(Res.string.stop) else stringResource(
                    Res.string.start
                ),
                onClick = {
                    if (status == RecordingStatus.Start) {
                        // Stop recording
                        status = RecordingStatus.Stop
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
        }
    }
}

private enum class RecordingStatus {
    Start,
    Stop
}

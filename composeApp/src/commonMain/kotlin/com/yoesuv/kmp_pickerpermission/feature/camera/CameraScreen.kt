package com.yoesuv.kmp_pickerpermission.feature.camera

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.camera
import kmppickerpermission.composeapp.generated.resources.open_camera
import org.jetbrains.compose.resources.stringResource

@Composable
fun CameraScreen(nav: NavHostController) {
    val permissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember(permissionsControllerFactory) {
        permissionsControllerFactory.createPermissionsController()
    }

    BindEffect(permissionsController)

    val viewModel: CameraViewModel = viewModel(
        factory = CameraViewModelFactory.create(permissionsController)
    )

    val photoPath by viewModel.photoPath.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val permissionState by viewModel.permissionState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.camera),
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
            // Display camera photo using Coil AsyncImage
            AsyncImage(
                model = photoPath,
                contentDescription = "Camera Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to open camera
            AppButton(
                text = stringResource(Res.string.open_camera),
                onClick = {
                    when (permissionState) {
                        PermissionState.Granted -> viewModel.takePhoto()
                        PermissionState.Denied, PermissionState.DeniedAlways -> viewModel.openAppSettings()
                        else -> viewModel.requestCameraPermission { viewModel.takePhoto() }
                    }
                },
                loading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

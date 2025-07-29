package com.yoesuv.kmp_pickerpermission.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import com.yoesuv.kmp_pickerpermission.core.route.AppRoute
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.camera
import kmppickerpermission.composeapp.generated.resources.date_time
import kmppickerpermission.composeapp.generated.resources.download_file
import kmppickerpermission.composeapp.generated.resources.file
import kmppickerpermission.composeapp.generated.resources.gallery
import kmppickerpermission.composeapp.generated.resources.home
import kmppickerpermission.composeapp.generated.resources.location
import kmppickerpermission.composeapp.generated.resources.notifications
import kmppickerpermission.composeapp.generated.resources.record_audio
import org.jetbrains.compose.resources.stringResource

private val BUTTON_BOTTOM_PADDING = 8.dp

@Composable
fun HomeScreen(nav: NavHostController) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.home),
                canBack = false
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                AppButton(
                    text = stringResource(Res.string.gallery),
                    onClick = {
                        nav.navigate(AppRoute.Gallery)
                    },
                    modifier = Modifier.padding(bottom = BUTTON_BOTTOM_PADDING)
                )

                AppButton(
                    text = stringResource(Res.string.camera),
                    onClick = {
                        nav.navigate(AppRoute.Camera)
                    },
                    modifier = Modifier.padding(bottom = BUTTON_BOTTOM_PADDING)
                )

                AppButton(
                    text = stringResource(Res.string.file),
                    onClick = {
                        nav.navigate(AppRoute.File)
                    },
                    modifier = Modifier.padding(bottom = BUTTON_BOTTOM_PADDING)
                )

                AppButton(
                    text = stringResource(Res.string.location),
                    onClick = {
                        // TODO: Setup location functionality
                    },
                    modifier = Modifier.padding(bottom = BUTTON_BOTTOM_PADDING)
                )

                AppButton(
                    text = stringResource(Res.string.record_audio),
                    onClick = {
                        // TODO: Setup record audio functionality
                    },
                    modifier = Modifier.padding(bottom = BUTTON_BOTTOM_PADDING)
                )

                AppButton(
                    text = stringResource(Res.string.notifications),
                    onClick = {
                        // TODO: Setup notifications functionality
                    },
                    modifier = Modifier.padding(bottom = BUTTON_BOTTOM_PADDING)
                )

                AppButton(
                    text = stringResource(Res.string.download_file),
                    onClick = {
                        // TODO: Setup download file functionality
                    },
                    modifier = Modifier.padding(bottom = BUTTON_BOTTOM_PADDING)
                )

                AppButton(
                    text = stringResource(Res.string.date_time),
                    onClick = {
                        // TODO: Setup date time functionality
                    }
                )
            }
        }
    }
}
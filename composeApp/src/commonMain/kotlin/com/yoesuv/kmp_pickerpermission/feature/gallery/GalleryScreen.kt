package com.yoesuv.kmp_pickerpermission.feature.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.gallery
import kmppickerpermission.composeapp.generated.resources.open_gallery
import org.jetbrains.compose.resources.stringResource

@Composable
fun GalleryScreen(nav: NavHostController) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.gallery),
                canBack = true,
                navigateUp = {
                    nav.navigateUp()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(24.dp)
        ) {
            AppButton(stringResource(Res.string.open_gallery), onClick = { })
        }
    }
}
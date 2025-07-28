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

@Composable
fun HomeScreen(nav: NavHostController) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Home",
                canBack = false
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                AppButton(
                    text = "Gallery",
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AppButton(
                    text = "Camera",
                    onClick = { /*TODO*/ }
                )
            }
        }
    }
}
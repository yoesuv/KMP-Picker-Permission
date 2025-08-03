package com.yoesuv.kmp_pickerpermission.feature.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.notification_status
import kmppickerpermission.composeapp.generated.resources.local_notification
import kmppickerpermission.composeapp.generated.resources.notifications
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationScreen(nav: NavHostController) {
    var notificationStatus by remember { mutableStateOf("Silent") } // Default status, will be updated based on actual status

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
                text = notificationStatus,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Local notification button
            AppButton(
                text = stringResource(Res.string.local_notification),
                onClick = {
                    // TODO: Implement local notification logic
                }
            )
        }
    }
}

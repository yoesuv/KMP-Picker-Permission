package com.yoesuv.kmp_pickerpermission.feature.datetime

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
import com.yoesuv.kmp_pickerpermission.components.AppDatePicker
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import com.yoesuv.kmp_pickerpermission.getCurrentPlatform
import com.yoesuv.kmp_pickerpermission.isAndroid
import com.yoesuv.kmp_pickerpermission.platform.pickDate
import com.yoesuv.kmp_pickerpermission.utils.formatDateFromMillis
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.date_picker
import kmppickerpermission.composeapp.generated.resources.date_time
import kmppickerpermission.composeapp.generated.resources.selected_date
import kmppickerpermission.composeapp.generated.resources.selected_time
import kmppickerpermission.composeapp.generated.resources.time_picker
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateTimeScreen(nav: NavHostController) {
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val currentPlatform = getCurrentPlatform()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.date_time),
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
                .fillMaxWidth()
        ) {
            // Selected Date Section
            Text(
                text = stringResource(Res.string.selected_date),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = selectedDateMillis?.let { formatDateFromMillis(it) } ?: "No date selected",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Date Picker Button - shown on both platforms
            AppButton(
                text = stringResource(Res.string.date_picker),
                onClick = {
                    if (currentPlatform.isAndroid) {
                        showDatePicker = true
                    } else {
                        // iOS: Use native date picker
                        pickDate { dateMillis ->
                            selectedDateMillis = dateMillis
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Selected Time Section
            Text(
                text = stringResource(Res.string.selected_time),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = selectedTime ?: "No time selected",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Time Picker Button
            AppButton(
                text = stringResource(Res.string.time_picker),
                onClick = {
                    // TODO: Implement time picker functionality
                    selectedTime = "12:00 PM" // Placeholder
                }
            )
        }
    }

    // Only show AppDatePicker on Android
    if (currentPlatform.isAndroid) {
        AppDatePicker(
            showDatePicker = showDatePicker,
            onDateSelected = { dateMillis ->
                selectedDateMillis = dateMillis
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

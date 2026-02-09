package com.yoesuv.kmp_pickerpermission.feature.datetime

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppDatePicker
import com.yoesuv.kmp_pickerpermission.components.AppTimePicker
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
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
    val viewModel: DateTimeViewModel = viewModel(
        factory = DateTimeViewModelFactory.create()
    )

    val selectedDateMillis by viewModel.selectedDateMillis.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()
    val showDatePicker by viewModel.showDatePicker.collectAsState()
    val showTimePicker by viewModel.showTimePicker.collectAsState()

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
                    viewModel.showDatePicker()
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
                    viewModel.showTimePicker()
                }
            )
        }
    }

    AppDatePicker(
        showDatePicker = showDatePicker,
        onDateSelected = { dateMillis ->
            viewModel.onDateSelected(dateMillis)
            viewModel.dismissDatePicker()
        },
        onDismiss = {
            viewModel.dismissDatePicker()
        }
    )

    // AppTimePicker for all platforms
    AppTimePicker(
        showTimePicker = showTimePicker,
        onTimeSelected = { hour, minute ->
            viewModel.onTimeSelected(hour, minute)
            viewModel.dismissTimePicker()
        },
        onDismiss = {
            viewModel.dismissTimePicker()
        }
    )
}

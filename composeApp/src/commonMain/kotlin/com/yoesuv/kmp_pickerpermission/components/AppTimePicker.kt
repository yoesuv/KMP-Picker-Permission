package com.yoesuv.kmp_pickerpermission.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePicker(
    showTimePicker: Boolean,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    modifier: Modifier = Modifier
) {
    val timePickerState = rememberTimePickerState()

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(timePickerState.hour, timePickerState.minute)
                    }
                ) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(dismissButtonText)
                }
            },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            modifier = modifier
        )
    }
}

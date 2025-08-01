package com.yoesuv.kmp_pickerpermission.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePicker(
    showDatePicker: Boolean,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel"
) {
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
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
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

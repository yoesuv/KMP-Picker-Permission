package com.yoesuv.kmp_pickerpermission.platform

// For Android, we'll keep using the existing AppDatePicker component
// This is just a placeholder implementation
actual fun pickDate(onDatePicked: (Long) -> Unit) {
    // Android implementation will use the existing AppDatePicker component
    // This function is not used on Android, but required for compilation
}

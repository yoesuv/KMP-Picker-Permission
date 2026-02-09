package com.yoesuv.kmp_pickerpermission.feature.datetime

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DateTimeViewModel : ViewModel() {

    private val _selectedDateMillis = MutableStateFlow<Long?>(null)
    val selectedDateMillis: StateFlow<Long?> = _selectedDateMillis.asStateFlow()

    private val _selectedTime = MutableStateFlow<String?>(null)
    val selectedTime: StateFlow<String?> = _selectedTime.asStateFlow()

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker.asStateFlow()

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker: StateFlow<Boolean> = _showTimePicker.asStateFlow()

    fun onDateSelected(dateMillis: Long?) {
        _selectedDateMillis.value = dateMillis
        _showDatePicker.value = false
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        println("Time selected - Hour: $hour, Minute: $minute")
        // Format time as HH:MM in 24-hour format
        val paddedHour = hour.toString().padStart(2, '0')
        val paddedMinute = minute.toString().padStart(2, '0')
        _selectedTime.value = "$paddedHour:$paddedMinute"
    }

    fun showDatePicker() {
        _showDatePicker.value = true
    }

    fun dismissDatePicker() {
        _showDatePicker.value = false
    }

    fun showTimePicker() {
        _showTimePicker.value = true
    }

    fun dismissTimePicker() {
        _showTimePicker.value = false
    }
}

package com.yoesuv.kmp_pickerpermission.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Formats a timestamp in milliseconds to a readable date string
 * @param millis The timestamp in milliseconds since epoch
 * @return Formatted date string in "day-Month-year" format (e.g., "17-August-2025")
 */
@OptIn(kotlin.time.ExperimentalTime::class)
fun formatDateFromMillis(millis: Long): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    
    val day = localDateTime.day
    val month =localDateTime.month.name
    val year = localDateTime.year
    
    return "$day-$month-$year"
}

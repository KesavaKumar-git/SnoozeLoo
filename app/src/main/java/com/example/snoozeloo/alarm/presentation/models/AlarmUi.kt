package com.example.snoozeloo.alarm.presentation.models

import java.time.ZonedDateTime

data class AlarmUi(
    val alarmName: String,
    val isActive: Boolean,
    val time: ZonedDateTime,
    val timeFormatted: String,
    val isAM: Boolean,
    val is24HrFormat: Boolean,
    val selectedDays: HashMap<Days, Boolean>
)

enum class Days(val displayableText: String)
{
    Monday("Mo"),
    Tuesday("Tu"),
    Wednesday("We"),
    Thursday("Th"),
    Friday("Fr"),
    Saturday("Sa"),
    Sunday("Su")
}
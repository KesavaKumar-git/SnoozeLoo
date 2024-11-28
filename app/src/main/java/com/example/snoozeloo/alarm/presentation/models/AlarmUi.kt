package com.example.snoozeloo.alarm.presentation.models

import android.net.Uri
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class AlarmUi(
    val alarmName: String = "",
    val isActive: Boolean = true,
    val time: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")),
    val shouldVibrate: Boolean = true,
    val timeFormatted: String = formatZonedDateTimeToTime(time.withZoneSameInstant(ZoneId.systemDefault())),
    val isAM: Boolean? =  time.withZoneSameInstant(ZoneId.systemDefault()).hour < 12,
    val volume: Int = 80,
    val selectedDays: MutableSet<Days> = mutableSetOf(),
    val alarmRingtone: AlarmSound?
)

data class AlarmSound(
    val title: String,
    val uri: Uri
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

fun formatZonedDateTimeToTime(zonedDateTime: ZonedDateTime): String
{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return zonedDateTime.format(formatter)
}
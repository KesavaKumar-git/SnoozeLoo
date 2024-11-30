package com.example.snoozeloo.alarm.presentation.models

import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import com.example.snoozeloo.core.database.table_entities.AlarmEntity
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class AlarmUi(
    val id: Int? = null,
    val alarmName: String = "Alarm",
    val isActive: Boolean = true,
    val time: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")).plusMinutes(1),
    val shouldVibrate: Boolean = true,
    val isAM: Boolean =  time.withZoneSameInstant(ZoneId.systemDefault()).hour < 12,
    val volume: Int = 80,
    val selectedDays: MutableSet<Days> = mutableSetOf(LocalDate.now().dayOfWeek.toDays()),
    val alarmRingtone: AlarmSound?
)

data class AlarmSound(
    val title: String,
    val uri: Uri
)

enum class Days(val displayableText: String)
{
    MONDAY("Mo"),
    TUESDAY("Tu"),
    WEDNESDAY("We"),
    THURSDAY("Th"),
    FRIDAY("Fr"),
    SATURDAY("Sa"),
    SUNDAY("Su")
}

fun Days.toDayOfWeek(): DayOfWeek
{
    return when(this)
    {
        Days.MONDAY -> DayOfWeek.MONDAY
        Days.TUESDAY -> DayOfWeek.TUESDAY
        Days.WEDNESDAY -> DayOfWeek.WEDNESDAY
        Days.THURSDAY -> DayOfWeek.THURSDAY
        Days.FRIDAY -> DayOfWeek.FRIDAY
        Days.SATURDAY -> DayOfWeek.SATURDAY
        Days.SUNDAY -> DayOfWeek.SUNDAY
    }
}

fun DayOfWeek.toDays(): Days
{
    return when(this)
    {
        DayOfWeek.MONDAY -> Days.MONDAY
        DayOfWeek.TUESDAY -> Days.TUESDAY
        DayOfWeek.WEDNESDAY -> Days.WEDNESDAY
        DayOfWeek.THURSDAY -> Days.THURSDAY
        DayOfWeek.FRIDAY -> Days.FRIDAY
        DayOfWeek.SATURDAY -> Days.SATURDAY
        DayOfWeek.SUNDAY -> Days.SUNDAY
    }
}

fun formatZonedDateTimeToTime(context: Context, zonedDateTime: ZonedDateTime): String
{
    val time = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(if (!DateFormat.is24HourFormat(context)) "hh:mm" else "HH:mm")

    return time.format(formatter)

}

fun AlarmUi.toAlarmEntity(): AlarmEntity
{
    return AlarmEntity(
        alarmName = alarmName,
        ringtoneUri = alarmRingtone?.uri.toString(),
        ringtoneName = alarmRingtone?.title?: "",
        shouldVibrate = shouldVibrate,
        volume = volume,
        time = time.toInstant().toEpochMilli(),
        isActive = isActive,
        selectedDays = selectedDays.map { it.name },
        isAM = isAM
    ).let {
        if (id != null)
            it.copy(id = id)
        else
            it
    }
}

fun AlarmEntity.toAlarmUi(): AlarmUi
{
    return AlarmUi(
        id = id,
        alarmName = alarmName,
        isActive = isActive,
        time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("UTC")),
        shouldVibrate = shouldVibrate,
        isAM = isAM,
        volume = volume,
        selectedDays = selectedDays.mapNotNull {
            try {
                Days.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }.toMutableSet(),
        alarmRingtone = AlarmSound(ringtoneName, Uri.parse(ringtoneUri))
    )
}
package com.example.snoozeloo.core.domain.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.example.snoozeloo.core.presentation.utils.RingtoneManagerUtil
import com.example.snoozeloo.core.receiver.AlarmReceiver
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object AlarmConfigureManager
{
    fun configureAlarm(context: Context, time: ZonedDateTime, dayOfWeek: DayOfWeek, id: Int)
    {
        val localTime = time
            .withZoneSameInstant(ZoneId.systemDefault())
            .withSecond(0)
            .with(TemporalAdjusters.nextOrSame(dayOfWeek))
            .let {
                if (it.isBefore(ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault())))
                    it.plusWeeks(1)
                else
                    it
            }

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarm_id", id)
        intent.putExtra("alarm_time", getLabelFormattedTime(context = context, time = localTime))

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        setAlarmForDay(context = context, time = localTime.toInstant().toEpochMilli(), pendingIntent = pendingIntent)
    }

    private fun setAlarmForDay(context: Context, time: Long, pendingIntent: PendingIntent)
    {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY * 7, pendingIntent)
    }

    fun cancelAlarm(context: Context, id: Int)
    {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

    fun snoozeAfter5Min(context: Context, id: Int, label: String)
    {
        RingtoneManagerUtil.stopRingtone()

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarm_id", id)
        intent.putExtra("alarm_time", label)

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmClockInfo = AlarmManager.AlarmClockInfo(ZonedDateTime.now().plusMinutes(5).toInstant().toEpochMilli(), pendingIntent)

        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    fun getLabelFormattedTime(context: Context, time: ZonedDateTime): String
    {
        val localZonedDateTime = time.withZoneSameInstant(ZoneId.systemDefault())

        val is24HourFormat = DateFormat.is24HourFormat(context)

        val formatter = DateTimeFormatter.ofPattern(if (is24HourFormat) { "EEE HH:mm" } else { "EEE hh:mm a" }, Locale.getDefault())

        return localZonedDateTime.format(formatter)
    }
}
package com.example.snoozeloo.core.domain.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.example.snoozeloo.alarm.presentation.models.AlarmUi
import com.example.snoozeloo.core.presentation.utils.RingtoneManagerUtil
import com.example.snoozeloo.core.receiver.AlarmReceiver
import java.time.DayOfWeek
import java.time.LocalDate
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

        val pendingIntent = PendingIntent.getBroadcast(context, generateUniqueRequestCode(id, time.toInstant().toEpochMilli(), dayOfWeek), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        setAlarmForDay(context = context, time = localTime.toInstant().toEpochMilli(), pendingIntent = pendingIntent)
    }

    private fun setAlarmForDay(context: Context, time: Long, pendingIntent: PendingIntent)
    {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun cancelAlarm(context: Context, alarmUi: AlarmUi)
    {
        alarmUi.id?.let {

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, AlarmReceiver::class.java)

            for (day in DayOfWeek.entries)
            {
                val pendingIntent = PendingIntent.getBroadcast(context, generateUniqueRequestCode(it, alarmUi.time.toInstant().toEpochMilli(), day), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                alarmManager.cancel(pendingIntent)
            }
        }
    }

    fun snoozeAfter5Min(context: Context, id: Int, label: String)
    {
        RingtoneManagerUtil.stopRingtone()

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarm_id", id)
        intent.putExtra("alarm_time", label)

        val time = ZonedDateTime.now().plusMinutes(5).toInstant().toEpochMilli()
        val pendingIntent = PendingIntent.getBroadcast(context, generateUniqueRequestCode(id, time, LocalDate.now().dayOfWeek), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmClockInfo = AlarmManager.AlarmClockInfo(time, pendingIntent)

        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)

    }

    fun getLabelFormattedTime(context: Context, time: ZonedDateTime): String
    {
        val localZonedDateTime = time.withZoneSameInstant(ZoneId.systemDefault())

        val is24HourFormat = DateFormat.is24HourFormat(context)

        val formatter = DateTimeFormatter.ofPattern(if (is24HourFormat) { "EEE HH:mm" } else { "EEE hh:mm a" }, Locale.getDefault())

        return localZonedDateTime.format(formatter)
    }

    private fun generateUniqueRequestCode(id: Int, time: Long, day: DayOfWeek): Int {
        return (id * 100000) + time.hashCode()  + day.hashCode()
    }
}
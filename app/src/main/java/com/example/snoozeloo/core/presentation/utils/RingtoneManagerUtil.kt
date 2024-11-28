package com.example.snoozeloo.core.presentation.utils

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import com.example.snoozeloo.alarm.presentation.models.AlarmSound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object RingtoneManagerUtil
{
    fun getAllAlarmSounds(context: Context): List<AlarmSound>
    {
        val ringtoneManager = RingtoneManager(context).apply { setType(RingtoneManager.TYPE_ALARM) }

        val alarmSounds = mutableListOf<AlarmSound>()
        val cursor = ringtoneManager.cursor

        cursor.use {
            while (it.moveToNext()) {
                val title = it.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                val uri = ringtoneManager.getRingtoneUri(it.position)

                alarmSounds.add(AlarmSound(title, uri))
            }
        }

        return alarmSounds
    }

    fun getDefaultAlarmSound(context: Context): AlarmSound?
    {
        val defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val defaultRingtone = RingtoneManager.getRingtone(context, defaultUri)

        return defaultUri?.let { AlarmSound(defaultRingtone.getTitle(context), it) }
    }

    fun playRingtone(context: Context, ringtoneUri: Uri?)
    {
        ringtoneUri?.let {

            val ringtone = RingtoneManager.getRingtone(context, it)

            ringtone.play()

            CoroutineScope(Dispatchers.IO).launch {
                delay(5000)
                ringtone.stop()
            }
        }
    }
}
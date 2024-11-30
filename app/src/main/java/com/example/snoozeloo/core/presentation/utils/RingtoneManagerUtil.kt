package com.example.snoozeloo.core.presentation.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.snoozeloo.alarm.presentation.models.AlarmSound

object RingtoneManagerUtil
{
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

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

    fun playRingtone(context: Context, ringtoneUri: Uri?, volume: Float = 1f)
    {
        mediaPlayer?.release()


        mediaPlayer = MediaPlayer().apply {

            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            setDataSource(context, ringtoneUri?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

            setVolume(volume, volume)

            prepare()

            start()
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

            vibrator = vibratorManager.defaultVibrator

            vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(500, 500), intArrayOf(255, 0), 0))
        }
    }

    fun stopRingtone()
    {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }

        mediaPlayer = null

        vibrator?.cancel()
    }
}
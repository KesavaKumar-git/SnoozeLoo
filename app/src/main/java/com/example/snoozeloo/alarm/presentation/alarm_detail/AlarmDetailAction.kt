package com.example.snoozeloo.alarm.presentation.alarm_detail

import android.content.Context
import com.example.snoozeloo.alarm.presentation.models.AlarmUi
import com.example.snoozeloo.alarm.presentation.models.Days

sealed interface AlarmDetailAction
{
    data class OnSaveAlarm(val context: Context, val alarm: AlarmUi): AlarmDetailAction

    data object OnClose: AlarmDetailAction

    data class OnDaySelected(val day: Days): AlarmDetailAction

    data class OnVolumeChanged(val volume: Float): AlarmDetailAction

    data class OnVibrateToggled(var isEnabled: Boolean): AlarmDetailAction

    data object OnSelectRingtone: AlarmDetailAction

    data class AlarmNameSelected(val alarmName: String): AlarmDetailAction
}
package com.example.snoozeloo.alarm.presentation.alarm_snooze

import android.content.Context
import com.example.snoozeloo.alarm.presentation.models.AlarmUi

sealed interface AlarmSnoozeAction
{
    data object OnAlarmTurnOff: AlarmSnoozeAction

    data class OnAlarmSnooze(val id: Int, val label: String, val context: Context): AlarmSnoozeAction

    data class OnAlarmLaunch(val alarmUi: AlarmUi, val context: Context): AlarmSnoozeAction
}
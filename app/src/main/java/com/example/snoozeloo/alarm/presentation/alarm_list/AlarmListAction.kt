package com.example.snoozeloo.alarm.presentation.alarm_list

import android.content.Context
import com.example.snoozeloo.alarm.presentation.models.AlarmUi

sealed interface AlarmListAction
{
    data class OnAlarmClick(val alarm: AlarmUi): AlarmListAction

    data object OnAlarmCreate: AlarmListAction

    data class OnAlarmEnabled(val alarm: AlarmUi, val isEnabled: Boolean): AlarmListAction
}
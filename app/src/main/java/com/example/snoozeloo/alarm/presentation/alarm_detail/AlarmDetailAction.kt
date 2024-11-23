package com.example.snoozeloo.alarm.presentation.alarm_detail

import com.example.snoozeloo.alarm.presentation.models.AlarmUi

sealed interface AlarmDetailAction
{
    data class OnSaveAlarm(val alarm: AlarmUi): AlarmDetailAction

    data object OnClose: AlarmDetailAction
}
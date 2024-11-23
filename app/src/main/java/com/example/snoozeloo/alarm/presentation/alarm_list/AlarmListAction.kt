package com.example.snoozeloo.alarm.presentation.alarm_list

import com.example.snoozeloo.alarm.presentation.models.AlarmUi

sealed interface AlarmListAction
{
    data class OnAlarmClick(val alarm: AlarmUi): AlarmListAction
}
package com.example.snoozeloo.alarm.presentation.alarm_list

import com.example.snoozeloo.alarm.presentation.models.AlarmUi

data class AlarmListState(
    val alarms: List<AlarmUi> = emptyList(),
    val selectedAlarm: AlarmUi? = null
)
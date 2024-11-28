package com.example.snoozeloo.alarm.presentation.ringtone_list

import android.content.Context
import com.example.snoozeloo.alarm.presentation.models.AlarmSound

interface RingtoneListAction
{
    data class OnSelectRingtone(val context: Context ,val alarmSound: AlarmSound): RingtoneListAction

    data object OnClose: RingtoneListAction
}

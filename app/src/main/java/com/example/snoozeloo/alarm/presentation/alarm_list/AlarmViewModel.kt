package com.example.snoozeloo.alarm.presentation.alarm_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snoozeloo.alarm.presentation.alarm_detail.AlarmDetailAction
import com.example.snoozeloo.alarm.presentation.alarm_snooze.AlarmSnoozeAction
import com.example.snoozeloo.alarm.presentation.ringtone_list.RingtoneListAction
import com.example.snoozeloo.alarm.presentation.models.AlarmUi
import com.example.snoozeloo.alarm.presentation.models.toAlarmEntity
import com.example.snoozeloo.alarm.presentation.models.toAlarmUi
import com.example.snoozeloo.alarm.presentation.models.toDayOfWeek
import com.example.snoozeloo.core.database.SnoozeLooDatabase
import com.example.snoozeloo.core.domain.utils.AlarmConfigureManager
import com.example.snoozeloo.core.presentation.utils.RingtoneManagerUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmViewModel: ViewModel()
{
    private val _state = MutableStateFlow(AlarmListState())
    val state = _state
//        .onStart {  }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000L),
//            AlarmListState()
//        )

    fun getAlarms(context: Context)
    {
        viewModelScope.launch {
            SnoozeLooDatabase.getInstance(context = context).alarmsDao().getAlarmList().collectLatest { alarmList ->
                _state.update { it.copy(alarms = alarmList.map { it.toAlarmUi() })  }
            }
        }
    }

    suspend fun getAlarm(id: Int, context: Context)
    {
        SnoozeLooDatabase.getInstance(context = context).alarmsDao().getAlarm(id).collectLatest { alarmList ->
            _state.update { it.copy(selectedAlarm = alarmList?.toAlarmUi())  }
        }
    }

    fun onAction(action: AlarmListAction) {
        when (action) {
            is AlarmListAction.OnAlarmClick ->
            {
                _state.update { it.copy(selectedAlarm = action.alarm) }
            }

            is AlarmListAction.OnAlarmCreate ->
            {
                _state.update { it.copy(selectedAlarm = AlarmUi(alarmName = "", alarmRingtone = RingtoneManagerUtil.getDefaultAlarmSound(context = action.context) )) }
            }

            is AlarmListAction.OnAlarmEnabled ->
            {
                viewModelScope.launch {
                    action.alarm.id?.let {
                        SnoozeLooDatabase.getInstance(context = action.context).alarmsDao().toggleAlarm(action.isEnabled, it)
                    }
                }

                if (action.isEnabled)
                {
                    configureAlarmForAllDays(context = action.context, alarm = action.alarm)
                }
                else
                {
                    AlarmConfigureManager.cancelAlarm(context = action.context, id = action.alarm.id ?: -1)
                }
            }
        }
    }

    fun onAction(action: AlarmDetailAction)
    {
        when (action)
        {
            is AlarmDetailAction.OnSaveAlarm ->
            {
                _state.update { it.copy(selectedAlarm = null) }
                viewModelScope.launch {
                    SnoozeLooDatabase.getInstance(context = action.context).alarmsDao().insert(action.alarm.toAlarmEntity())
                }

                configureAlarmForAllDays(context = action.context, alarm = action.alarm)
            }

            is AlarmDetailAction.OnClose ->
            {
                _state.update { it.copy(selectedAlarm = null) }
            }

            is AlarmDetailAction.OnDaySelected ->
            {
                _state.update {
                    if (it.selectedAlarm != null)
                    {
                        it.copy(
                            selectedAlarm = it.selectedAlarm.copy(
                                selectedDays = it.selectedAlarm.selectedDays.toMutableSet().also { days ->
                                    if (days.contains(action.day))
                                    {
                                        days.remove(action.day)
                                    }
                                    else
                                    {
                                        days.add(action.day)
                                    }
                                }
                            )
                        )
                    }
                    else
                    {
                        it
                    }
                }
            }

            is AlarmDetailAction.OnVolumeChanged ->
            {
                _state.update { it.copy(selectedAlarm = it.selectedAlarm?.copy(volume = action.volume.toInt())) }
            }

            is AlarmDetailAction.OnVibrateToggled ->
            {
                _state.update { it.copy(selectedAlarm = it.selectedAlarm?.copy(shouldVibrate = action.isEnabled)) }
            }

            is AlarmDetailAction.OnSelectRingtone ->
            {
                _state.update { it.copy(canPickRingtone = true) }
            }

            is AlarmDetailAction.AlarmNameSelected ->
            {
                _state.update { it.copy(selectedAlarm = it.selectedAlarm?.copy(alarmName = action.alarmName)) }
            }
        }
    }

    fun onAction(action: RingtoneListAction)
    {
        when (action)
        {
            is RingtoneListAction.OnSelectRingtone ->
            {
                _state.update { it.copy(selectedAlarm = it.selectedAlarm?.copy(alarmRingtone = action.alarmSound)) }
                RingtoneManagerUtil.playRingtone(context = action.context, ringtoneUri = action.alarmSound.uri)
            }

            is RingtoneListAction.OnClose ->
            {
                _state.update { it.copy(canPickRingtone = false) }
            }
        }
    }

    fun onAction(action: AlarmSnoozeAction)
    {
        when (action)
        {
            is AlarmSnoozeAction.OnAlarmSnooze ->
            {
                AlarmConfigureManager.snoozeFor5Min(action.context, action.id, label = action.label)
            }

            is AlarmSnoozeAction.OnAlarmTurnOff ->
            {
                RingtoneManagerUtil.stopRingtone()
            }

            is AlarmSnoozeAction.OnAlarmLaunch ->
            {
                RingtoneManagerUtil.playRingtone(context = action.context, ringtoneUri = action.alarmUi.alarmRingtone?.uri, volume = (action.alarmUi.volume/100f))
            }
        }
    }

    private fun configureAlarmForAllDays(context: Context, alarm: AlarmUi)
    {
        for (day in alarm.selectedDays)
        {
            AlarmConfigureManager.configureAlarm(
                context = context,
                time = alarm.time,
                dayOfWeek = day.toDayOfWeek(),
                id = alarm.id ?: -1)
        }
    }

    fun getAllRingtones(context: Context) = RingtoneManagerUtil.getAllAlarmSounds(context = context)
}
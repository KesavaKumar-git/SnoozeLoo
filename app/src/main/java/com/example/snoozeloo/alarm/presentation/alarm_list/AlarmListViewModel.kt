package com.example.snoozeloo.alarm.presentation.alarm_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snoozeloo.alarm.presentation.alarm_detail.AlarmDetailAction
import com.example.snoozeloo.alarm.presentation.ringtone_list.RingtoneListAction
import com.example.snoozeloo.alarm.presentation.models.AlarmUi
import com.example.snoozeloo.core.presentation.utils.RingtoneManagerUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AlarmListViewModel: ViewModel()
{
    private val _state = MutableStateFlow(AlarmListState())
    val state = _state
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AlarmListState()
        )

    fun onAction(action: AlarmListAction) {
        when (action) {
            is AlarmListAction.OnAlarmClick -> {
                _state.update { it.copy(selectedAlarm = action.alarm) }
            }

            is AlarmListAction.OnAlarmCreate -> {
                _state.update { it.copy(selectedAlarm = AlarmUi(alarmName = "", alarmRingtone = RingtoneManagerUtil.getDefaultAlarmSound(context = action.context) )) }
            }
        }
    }

    fun onAction(action: AlarmDetailAction) {
        when (action) {
            is AlarmDetailAction.OnSaveAlarm,
            AlarmDetailAction.OnClose ->
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

    fun onAction(action: RingtoneListAction) {
        when (action) {
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

    fun getAllRingtones(context: Context) = RingtoneManagerUtil.getAllAlarmSounds(context = context)
}
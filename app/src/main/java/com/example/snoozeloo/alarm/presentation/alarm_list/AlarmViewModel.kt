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
import com.example.snoozeloo.core.database.dao.AlarmsDao
import com.example.snoozeloo.core.domain.utils.AlarmConfigureManager
import com.example.snoozeloo.core.domain.utils.NotificationUtil
import com.example.snoozeloo.core.presentation.utils.RingtoneManagerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(@ApplicationContext var context: Context, private val alarmsDao: AlarmsDao): ViewModel()
{
    private val _state = MutableStateFlow(AlarmListState())
    val state = _state
        .onStart { getAlarms() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AlarmListState()
        )

    private val _snoozeScreenState: MutableStateFlow<AlarmUi?> = MutableStateFlow(null)
    val snoozeScreenState = _snoozeScreenState.asStateFlow()

    private fun getAlarms()
    {
        viewModelScope.launch {
            alarmsDao.getAlarmList().collectLatest { alarmList ->
                _state.update { it.copy(alarms = alarmList.map { it.toAlarmUi() })  }
            }
        }
    }

    suspend fun getAlarm(id: Int)
    {
        viewModelScope.launch {
            alarmsDao.getAlarm(id).collectLatest { alarm ->
                _snoozeScreenState.value = alarm?.toAlarmUi()
            }
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
                _state.update { it.copy(selectedAlarm = AlarmUi(alarmRingtone = RingtoneManagerUtil.getDefaultAlarmSound(context = context) )) }
            }

            is AlarmListAction.OnAlarmEnabled ->
            {
                viewModelScope.launch {
                    action.alarm.id?.let {
                        alarmsDao.toggleAlarm(action.isEnabled, it)
                    }
                }

                if (action.isEnabled)
                {
                    configureAlarmForAllDays(context = context, alarm = action.alarm)
                }
                else
                {
                    AlarmConfigureManager.cancelAlarm(context = context, alarmUi = action.alarm)
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
                val alrm = action.alarm.toAlarmEntity()
                _state.update { it.copy(selectedAlarm = null) }

                viewModelScope.launch {
                    configureAlarmForAllDays(context = context, alarm = action.alarm.copy(id = alarmsDao.insert(alrm).toInt()))
                }
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
                RingtoneManagerUtil.playRingtone(context = context, ringtoneUri = action.alarmSound.uri)
            }

            is RingtoneListAction.OnClose ->
            {
                _state.update { it.copy(canPickRingtone = false) }
            }

            is RingtoneListAction.OnStopRingtone ->
            {
                RingtoneManagerUtil.stopRingtone()
            }
        }
    }

    fun onAction(action: AlarmSnoozeAction)
    {
        when (action)
        {
            is AlarmSnoozeAction.OnAlarmSnooze ->
            {
                AlarmConfigureManager.snoozeAfter5Min(context = context, action.id, label = action.label)
                NotificationUtil.removeNotification(context = context, notificationId = 1)
            }

            is AlarmSnoozeAction.OnAlarmTurnOff ->
            {
                RingtoneManagerUtil.stopRingtone()
                NotificationUtil.removeNotification(context = context, notificationId = 1)
            }

            is AlarmSnoozeAction.OnAlarmLaunch ->
            {
                AlarmConfigureManager.configureAlarm(context = context, time = action.alarmUi.time, dayOfWeek = LocalDate.now().dayOfWeek, id = action.alarmUi.id ?: -1)
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
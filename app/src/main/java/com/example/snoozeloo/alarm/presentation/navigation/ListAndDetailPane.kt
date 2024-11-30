package com.example.snoozeloo.alarm.presentation.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.snoozeloo.alarm.presentation.alarm_detail.AlarmDetailAction
import com.example.snoozeloo.alarm.presentation.alarm_detail.AlarmDetailsScreen
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmListAction
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmListScreen
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmViewModel
import com.example.snoozeloo.alarm.presentation.ringtone_list.RingtoneListAction
import com.example.snoozeloo.alarm.presentation.ringtone_list.RingtoneListScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListAndDetailPane(
    modifier: Modifier = Modifier,
    viewModel: AlarmViewModel
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.getAlarms(context)
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                AlarmListScreen(
                    state = state,
                    onAction = { action ->

                        viewModel.onAction(action)

                        when(action) {
                            is AlarmListAction.OnAlarmClick,
                            is AlarmListAction.OnAlarmCreate ->
                            {
                                navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                            }

                            is AlarmListAction.OnAlarmEnabled -> { }
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane{
                AlarmDetailsScreen(
                    alarm = state.selectedAlarm,
                    onAction = { action ->

                        viewModel.onAction(action)

                        when(action)
                        {
                            is AlarmDetailAction.OnSaveAlarm,
                            AlarmDetailAction.OnClose ->
                            {
                                navigator.navigateBack()
                            }

                            is AlarmDetailAction.OnSelectRingtone ->
                            {
                                navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Extra)
                            }
                            else -> { }
                        }
                    }
                )
            }
        },
        extraPane = {
            AnimatedPane{
                val context = LocalContext.current

                RingtoneListScreen(
                    selectAlarm = state.canPickRingtone,
                    alarmRingtones = viewModel.getAllRingtones(context = context),
                    selectedRingtone = state.selectedAlarm?.alarmRingtone,
                    onAction = { action ->

                        viewModel.onAction(action)

                        when(action)
                        {
                            is RingtoneListAction.OnClose ->
                            {
                                navigator.navigateBack()
                            }
                        }
                    }
                )
            }
        },
        modifier = modifier
    )

}
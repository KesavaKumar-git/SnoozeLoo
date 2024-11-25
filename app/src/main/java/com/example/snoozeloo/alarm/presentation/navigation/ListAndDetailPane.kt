package com.example.snoozeloo.alarm.presentation.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.snoozeloo.alarm.presentation.alarm_detail.AlarmDetailsScreen
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmListAction
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmListScreen
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmListState
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmListViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListAndDetailPane(
    modifier: Modifier = Modifier,
    viewModel: AlarmListViewModel = AlarmListViewModel()
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                AlarmListScreen(
                    state = AlarmListState(),
                    onAction = { action ->

                        viewModel.onAction(action)

                        when(action) {
                            is AlarmListAction.OnAlarmClick -> {
                                navigator.navigateTo(
                                    pane = ListDetailPaneScaffoldRole.Detail
                                )
                            }
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane{
                AlarmDetailsScreen(
                    alarm = AlarmListState().selectedAlarm,
                    onAction = { action -> viewModel.onAction(action) }
                )
            }
        },
        modifier = modifier
    )

}
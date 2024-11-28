package com.example.snoozeloo.alarm.presentation.alarm_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.snoozeloo.R
import com.example.snoozeloo.alarm.presentation.alarm_list.components.AlarmListItem
import com.example.snoozeloo.ui.theme.SnoozeLooTheme

@Composable
fun AlarmListScreen(
    state: AlarmListState,
    onAction: (action: AlarmListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.your_alarms),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (state.alarms.isEmpty())
        {
            ListEmptyScreen()
        }
        else
        {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.alarms) { alarms ->

                    AlarmListItem(
                        alarmUi = alarms,
                        onClick = { onAction(AlarmListAction.OnAlarmClick(alarms)) }
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier.padding(16.dp).fillMaxSize()
    ) {
        val context = LocalContext.current

        FloatingActionButton(
            onClick = { onAction(AlarmListAction.OnAlarmCreate(context = context)) },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.create_alarm))
        }
    }
}

@PreviewLightDark
@Preview(showBackground = true)
@Composable
fun AlarmListScreenPreview()
{
    SnoozeLooTheme {
        Scaffold { innerPadding ->
            AlarmListScreen(
                state = AlarmListState(),
                onAction = {},
                modifier = Modifier.padding(innerPadding))
        }
    }
}
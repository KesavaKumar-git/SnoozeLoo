package com.example.snoozeloo.alarm.presentation.alarm_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.your_alarms),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).padding(horizontal = 16.dp)
        )

        if (state.alarms.isEmpty())
        {
            ListEmptyScreen()
        }
        else
        {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(state.alarms) { alarm ->

                    AlarmListItem(
                        alarmUi = alarm,
                        onClick = { onAction(AlarmListAction.OnAlarmClick(alarm)) },
                        onToggle = { isEnabled -> onAction(AlarmListAction.OnAlarmEnabled(context = context, alarm = alarm, isEnabled = isEnabled)) }
                    )
                }

                item { Spacer(Modifier.size(32.dp)) }
            }
        }
    }

    Box(
        modifier = modifier.padding(16.dp).fillMaxSize()
    ) {

        FloatingActionButton(
            onClick = { onAction(AlarmListAction.OnAlarmCreate(context = context)) },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.create_alarm))
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
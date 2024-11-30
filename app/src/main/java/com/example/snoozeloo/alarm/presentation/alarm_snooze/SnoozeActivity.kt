package com.example.snoozeloo.alarm.presentation.alarm_snooze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmViewModel
import com.example.snoozeloo.ui.theme.SnoozeLooTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SnoozeActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            SnoozeLooTheme {

                SnoozeScreen(
                    alarmId = intent.getIntExtra("alarm_id", -1),
                    onClose = { finish() }
                )
            }
        }
    }
}

@Composable
fun SnoozeScreen(
    alarmId: Int,
    onClose: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        val state by viewModel.snoozeScreenState.collectAsStateWithLifecycle()

        LaunchedEffect(true) { viewModel.getAlarm(alarmId) }

        val context = LocalContext.current

        state?.let {

            LaunchedEffect(true) {
                viewModel.onAction(AlarmSnoozeAction.OnAlarmLaunch(it, context))
            }

            AlarmSnoozeScreen(
                alarm = it,
                modifier = Modifier.padding(innerPadding),
                onAction = {

                    viewModel.onAction(it)

                    when(it)
                    {
                        is AlarmSnoozeAction.OnAlarmSnooze, AlarmSnoozeAction.OnAlarmTurnOff ->
                        {
                            onClose()
                        }

                        else -> {}
                    }
                })
        }
    }
}
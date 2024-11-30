package com.example.snoozeloo.alarm.presentation.alarm_snooze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmViewModel
import com.example.snoozeloo.ui.theme.SnoozeLooTheme

class SnoozeActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        enableEdgeToEdge()
        setContent {

            SnoozeLooTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    val state by viewModel.state.collectAsStateWithLifecycle()

                    LaunchedEffect(true) {
                        viewModel.getAlarm(intent.getIntExtra("alarm_id", -1), this@SnoozeActivity)
                    }

                    state.selectedAlarm?.let {

                        LaunchedEffect(true) {
                            viewModel.onAction(AlarmSnoozeAction.OnAlarmLaunch(it, this@SnoozeActivity))
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
                                        finish()
                                    }

                                    else -> {}
                                }
                            })
                    }
                }
            }
        }
    }
}
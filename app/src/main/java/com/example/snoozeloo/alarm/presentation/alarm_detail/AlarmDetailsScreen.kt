package com.example.snoozeloo.alarm.presentation.alarm_detail

import android.transition.Slide
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.snoozeloo.R
import com.example.snoozeloo.alarm.presentation.alarm_list.AlarmListAction
import com.example.snoozeloo.alarm.presentation.components.DaysChip
import com.example.snoozeloo.alarm.presentation.components.LabelValueCard
import com.example.snoozeloo.alarm.presentation.components.LabelWithSwitch
import com.example.snoozeloo.alarm.presentation.models.AlarmUi
import com.example.snoozeloo.alarm.presentation.models.Days
import com.example.snoozeloo.ui.theme.SnoozeLooTheme
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailsScreen(
    alarm: AlarmUi?,
    onAction: (action: AlarmDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (alarm != null)
    {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp))
                        .padding(6.dp)
                        .clickable { AlarmDetailAction.OnClose },
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                TextButton(
                    onClick = { onAction(AlarmDetailAction.OnSaveAlarm(alarm)) },
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val timePickerState = rememberTimePickerState(is24Hour = true)
                    TimeInput(state = timePickerState)
                }
            }

            LabelValueCard(label = stringResource(R.string.alarm_name), value = alarm.alarmName)

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.repeat),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    DaysChip(selectedMap = alarm.selectedDays)
                }
            }

            LabelValueCard(label = stringResource(R.string.alarm_ringtone), value = stringResource(R.string.str_default))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)){

                    Text(
                        text = stringResource(R.string.alarm_volume),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp).padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    val sliderPosition by remember { mutableFloatStateOf(0.8f) }

                    Slider(
                        value = sliderPosition,
                        onValueChange = {},
                        colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.secondary)
                    )

                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    LabelWithSwitch(
                        label = stringResource(R.string.vibrate),
                        isEnabled = alarm.isActive,
                        onChanged = { }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AlarmDetailsScreenPreview(modifier: Modifier = Modifier)
{
    SnoozeLooTheme {
        AlarmDetailsScreen(AlarmUi(
            alarmName = "First Alarm",
            isActive = true,
            timeFormatted = "10:00",
            time = ZonedDateTime.now(),
            is24HrFormat = false,
            isAM = false,
            selectedDays = HashMap<Days, Boolean>().apply {
                put(Days.Monday, true)
            }),
            onAction = {}
        )
    }
}
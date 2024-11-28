package com.example.snoozeloo.alarm.presentation.alarm_list.components

import android.net.Uri
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.snoozeloo.R
import com.example.snoozeloo.alarm.presentation.components.DaysChip
import com.example.snoozeloo.alarm.presentation.components.LabelWithSwitch
import com.example.snoozeloo.alarm.presentation.models.AlarmSound
import com.example.snoozeloo.alarm.presentation.models.AlarmUi
import com.example.snoozeloo.alarm.presentation.models.Days
import com.example.snoozeloo.ui.theme.SnoozeLooTheme
import java.time.ZonedDateTime

@Composable
fun AlarmListItem(
    alarmUi: AlarmUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            LabelWithSwitch(
                label = alarmUi.alarmName,
                isChecked = alarmUi.isActive,
                onChanged = {onClick()}
            )

            Row (
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = alarmUi.timeFormatted,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(end = 5.dp)
                )

                if (!DateFormat.is24HourFormat(LocalContext.current))
                {
                    Text(
                        text = if (alarmUi.isAM == true) stringResource(R.string.am) else stringResource(R.string.pm),
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }

            DaysChip(modifier = Modifier.padding(top = 8.dp), selectedMap = alarmUi.selectedDays, onClick = {})
        }
    }
}


@PreviewLightDark
@Composable
fun AlarmListItemPreview()
{
    SnoozeLooTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                AlarmListItem(
                    alarmUi = AlarmUi(
                        alarmName = "First Alarm",
                        isActive = true,
                        timeFormatted = "10:00",
                        time = ZonedDateTime.now(),
                        isAM = false,
                        selectedDays = mutableSetOf(Days.Monday),
                        alarmRingtone = AlarmSound(title = "Default", uri = Uri.parse(""))
                    ),
                    onClick = {}
                )
            }
        }
    }
}
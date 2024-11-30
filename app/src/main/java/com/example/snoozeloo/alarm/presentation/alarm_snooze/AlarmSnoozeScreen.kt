package com.example.snoozeloo.alarm.presentation.alarm_snooze

import android.text.TextUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snoozeloo.R
import com.example.snoozeloo.alarm.presentation.models.AlarmUi
import com.example.snoozeloo.alarm.presentation.models.formatZonedDateTimeToTime
import com.example.snoozeloo.core.domain.utils.AlarmConfigureManager.getLabelFormattedTime
import com.example.snoozeloo.ui.theme.SnoozeLooTheme

@Composable
fun AlarmSnoozeScreen(
    alarm: AlarmUi,
    modifier: Modifier = Modifier,
    onAction: (action: AlarmSnoozeAction) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_alarm),
            modifier = Modifier.padding(bottom = 16.dp).size(64.dp),
            contentDescription = stringResource(R.string.alarm),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )

        Text(
            text = formatZonedDateTimeToTime(context, alarm.time),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 82.sp)

        if (!TextUtils.isEmpty(alarm.alarmName))
        {
            Text(
                text = alarm.alarmName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp))
        }

        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = { onAction(AlarmSnoozeAction.OnAlarmTurnOff) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.turn_off),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { alarm.id?.let { onAction(AlarmSnoozeAction.OnAlarmSnooze(id = it, label = getLabelFormattedTime(context, alarm.time))) } },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.snooze_btn),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@PreviewLightDark
@Preview(showBackground = true)
@Composable
fun AlarmSnoozeScreenPreview()
{
    SnoozeLooTheme {
        Surface {
//            AlarmSnoozeScreen(onAction = {})
        }
    }
}

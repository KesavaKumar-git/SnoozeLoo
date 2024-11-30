package com.example.snoozeloo.alarm.presentation.ringtone_list

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.snoozeloo.R
import com.example.snoozeloo.alarm.presentation.models.AlarmSound
import com.example.snoozeloo.ui.theme.SnoozeLooTheme

@Composable
fun RingtoneListScreen(
    selectAlarm: Boolean,
    alarmRingtones: List<AlarmSound>,
    selectedRingtone: AlarmSound?,
    onAction: (action: RingtoneListAction) -> Unit,
    modifier: Modifier = Modifier
) {

    if (selectAlarm)
    {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10.dp))
                        .padding(6.dp)
                        .clickable { onAction(RingtoneListAction.OnClose) },
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 32.dp, horizontal = 16.dp)
            ) {
                items(alarmRingtones) { ringtone ->

                    Card(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAction(RingtoneListAction.OnSelectRingtone(alarmSound = ringtone)) }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ringtone.title,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (ringtone.uri == selectedRingtone?.uri)
                            {
                                Image(
                                    painter = painterResource(R.drawable.ic_selected_rounded),
                                    contentDescription = stringResource(R.string.selected),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun RingtoneListScreenPreview(modifier: Modifier = Modifier)
{
    SnoozeLooTheme {
        Scaffold { innerPadding ->
            RingtoneListScreen(
                selectAlarm = true,
                alarmRingtones = listOf(AlarmSound("Ringtone 1", Uri.parse("")), AlarmSound("Ringtone 2", Uri.parse(""))),
                onAction = {},
                selectedRingtone = AlarmSound("Ringtone 1", Uri.parse("")),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
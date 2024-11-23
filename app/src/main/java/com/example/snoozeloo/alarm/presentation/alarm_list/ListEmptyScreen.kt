package com.example.snoozeloo.alarm.presentation.alarm_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snoozeloo.R
import com.example.snoozeloo.ui.theme.SnoozeLooTheme

@Composable
fun ListEmptyScreen()
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_alarm),
            contentDescription = stringResource(R.string.alarm),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.size(40.dp))

        Text(
            text = stringResource(R.string.empty_screen_msg),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@PreviewLightDark
@Composable
fun ListEmptyScreenPreview(modifier: Modifier = Modifier)
{
    SnoozeLooTheme {
        ListEmptyScreen()
    }
}
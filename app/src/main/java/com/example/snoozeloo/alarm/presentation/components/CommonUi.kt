package com.example.snoozeloo.alarm.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.snoozeloo.alarm.presentation.models.Days

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DaysChip(modifier: Modifier = Modifier, selectedMap: HashMap<Days, Boolean>)
{
    FlowRow(
        modifier = modifier
    ) {
        for (day in Days.entries)
        {
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = day.displayableText,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                enabled = selectedMap[day] == true,
                shape = RoundedCornerShape(20.dp),
                border = null,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledLabelColor = MaterialTheme.colorScheme.onSecondary
                )
            )

            Spacer(Modifier.padding(end = 5.dp))
        }
    }
}

@Composable
fun LabelWithSwitch(label: String, isEnabled: Boolean, onChanged: (Boolean) -> Unit, modifier: Modifier = Modifier)
{
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )

        Switch(
            checked = isEnabled,
            onCheckedChange = { onChanged(it) }
        )
    }
}

@Composable
fun LabelValueCard(label: String, value: String, modifier: Modifier = Modifier)
{
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.End
            )
        }
    }
}
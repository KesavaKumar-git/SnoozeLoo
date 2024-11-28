package com.example.snoozeloo.alarm.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.snoozeloo.alarm.presentation.models.Days
import com.example.snoozeloo.ui.theme.SnoozeLooTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DaysChip(modifier: Modifier = Modifier, selectedMap: Set<Days>, onClick: (Days) -> Unit)
{
    FlowRow(
        modifier = modifier
    ) {
        for (day in Days.entries)
        {
            AssistChip(
                onClick = { onClick(day) },
                label = {
                    Text(
                        text = day.displayableText,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                shape = RoundedCornerShape(20.dp),
                border = null,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selectedMap.contains(day)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    labelColor = if (selectedMap.contains(day)) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                )
            )

            Spacer(Modifier.padding(end = 5.dp))
        }
    }
}

@Composable
fun LabelWithSwitch(label: String, isChecked: Boolean, onChanged: (Boolean) -> Unit, modifier: Modifier = Modifier)
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
            checked = isChecked,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonAlertDialog(
    title: String = "Alarm Name",
    value: String,
    primaryButton : String = "Save",
    onConfirm : (String) -> Unit ={},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onSurface)

            var alarmName by remember { mutableStateOf(value) }

            EditTextField(
                value = alarmName,
                onValueChange = { alarmName = it }
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onConfirm(alarmName) }
                ) {
                    Text(
                        text = primaryButton,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun EditTextField(value: String, onValueChange:(String) -> Unit, modifier: Modifier = Modifier)
{

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyLarge,
        maxLines = 1
    )
}

@PreviewLightDark
@Composable
fun DialogPreview(modifier: Modifier = Modifier)
{
    SnoozeLooTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                CommonAlertDialog(value = "")
            }
        }
    }
    
}
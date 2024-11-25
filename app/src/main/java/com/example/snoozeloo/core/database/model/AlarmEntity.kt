package com.example.snoozeloo.core.database.model

import androidx.room.Entity

@Entity(tableName = "Alarms")
data class AlarmEntity(
    val alarmName: String,
    val isActive: Boolean,
    val time: Long,
    val timeFormatted: String,
    val isAM: Boolean,
    val is24HrFormat: Boolean,
    val selectedDays: ArrayList<String>
)
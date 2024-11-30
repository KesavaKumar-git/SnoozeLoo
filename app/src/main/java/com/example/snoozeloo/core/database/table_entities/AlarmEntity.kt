package com.example.snoozeloo.core.database.table_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0,

    val alarmName: String,

    val isActive: Boolean,

    val time: Long,

    val isAM: Boolean,

    val volume: Int,

    val shouldVibrate: Boolean,

    val selectedDays: List<String>,

    val ringtoneUri: String,

    val ringtoneName: String
)
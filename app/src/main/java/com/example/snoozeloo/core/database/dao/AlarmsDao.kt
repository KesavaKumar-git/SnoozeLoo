package com.example.snoozeloo.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.snoozeloo.core.database.table_entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmsDao
{
    @Query(value = "SELECT * FROM alarms")
    fun getAlarmList(): Flow<List<AlarmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: AlarmEntity): Long

    @Query("UPDATE alarms SET isActive = :isActive WHERE _id = :id")
    suspend fun toggleAlarm(isActive: Boolean, id: Int)

    @Query("SELECT isActive FROM alarms WHERE _id = :id")
    suspend fun isAlarmActive(id: Int): Boolean

    @Query("SELECT * FROM alarms WHERE _id = :id")
    fun getAlarm(id: Int): Flow<AlarmEntity?>
}


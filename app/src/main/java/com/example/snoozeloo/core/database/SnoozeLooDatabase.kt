package com.example.snoozeloo.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.snoozeloo.core.database.model.AlarmEntity

const val DB_VERSION = 1

@Database(
    entities = [AlarmEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
abstract class SnoozeLooDatabase: RoomDatabase()
{
    companion object
    {
        private var INSTANCE: SnoozeLooDatabase? = null

        // need to remove after implementing di
        fun getInstance(context: Context): SnoozeLooDatabase
        {
            synchronized(Any())
            {
                return INSTANCE ?: Room
                    .databaseBuilder(context, SnoozeLooDatabase::class.java, "SnoozeLooDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
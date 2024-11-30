package com.example.snoozeloo.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.snoozeloo.core.database.dao.AlarmsDao
import com.example.snoozeloo.core.database.table_entities.AlarmEntity


const val DB_VERSION = 1

@Database(
    entities = [AlarmEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class SnoozeLooDatabase: RoomDatabase()
{
    abstract fun alarmsDao(): AlarmsDao

    companion object
    {
        private var INSTANCE: SnoozeLooDatabase? = null

        // need to remove after implementing di
        fun getInstance(context: Context): SnoozeLooDatabase
        {
            synchronized(Any())
            {
                return INSTANCE ?: Room
                    .databaseBuilder(context.applicationContext, SnoozeLooDatabase::class.java, "SnoozeLooDB")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

class StringListConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String?): List<String>? {
        return data?.split(",")?.filter { it.isNotBlank() }
    }
}
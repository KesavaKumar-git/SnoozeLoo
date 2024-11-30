package com.example.snoozeloo.core.database.di

import com.example.snoozeloo.SnoozeLooApp
import com.example.snoozeloo.core.database.SnoozeLooDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule
{
    @Provides
    fun providesAlarmsDao(database: SnoozeLooDatabase) = database.alarmsDao()
}
package com.example.snoozeloo.core.database.di

import android.content.Context
import com.example.snoozeloo.core.database.SnoozeLooDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesSnoozeLooDatabase(@ApplicationContext context: Context) = SnoozeLooDatabase.getInstance(context)
}

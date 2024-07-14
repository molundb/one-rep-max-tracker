package net.martinlundberg.onerepmaxtracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.data.database.OneRepMaxTrackerDatabase
import net.martinlundberg.onerepmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): OneRepMaxTrackerDatabase {
        return Room.databaseBuilder(
            appContext,
            OneRepMaxTrackerDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    fun providesMovementDao(database: OneRepMaxTrackerDatabase): MovementDao = database.movementDao()

    @Provides
    fun providesResultDao(database: OneRepMaxTrackerDatabase): ResultDao = database.resultDao()
}
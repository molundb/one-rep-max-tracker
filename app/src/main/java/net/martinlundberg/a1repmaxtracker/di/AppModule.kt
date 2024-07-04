package net.martinlundberg.a1repmaxtracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.a1repmaxtracker.data.database.OneRepMaxTrackerDatabase
import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.dao.OneRMDao
import net.martinlundberg.a1repmaxtracker.data.repository.DefaultMovementsRepository
import net.martinlundberg.a1repmaxtracker.data.repository.DefaultOneRepMaxRepository
import net.martinlundberg.a1repmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.a1repmaxtracker.data.repository.OneRepMaxRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesMovementDao(
        database: OneRepMaxTrackerDatabase,
    ): MovementDao = database.movementDao()

    @Provides
    fun providesOneRMDao(
        database: OneRepMaxTrackerDatabase,
    ): OneRMDao = database.oneRMDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): OneRepMaxTrackerDatabase {
        return Room.databaseBuilder(
            appContext,
            OneRepMaxTrackerDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovementsRepository(db: OneRepMaxTrackerDatabase): MovementsRepository {
        return DefaultMovementsRepository(db.movementDao())
    }

    @Provides
    @Singleton
    fun provideOneRepMaxRepository(db: OneRepMaxTrackerDatabase): OneRepMaxRepository {
        return DefaultOneRepMaxRepository(db.oneRMDao())
    }
}
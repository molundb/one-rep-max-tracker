package net.martinlundberg.onerepmaxtracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.AnalyticsService
import net.martinlundberg.onerepmaxtracker.data.DataStorePreferences
import net.martinlundberg.onerepmaxtracker.data.database.OneRepMaxTrackerDatabase
import net.martinlundberg.onerepmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultMovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultResultRepository
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.util.WeightUnitService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesMovementDao(
        database: OneRepMaxTrackerDatabase,
    ): MovementDao = database.movementDao()

    @Provides
    fun providesResultDao(
        database: OneRepMaxTrackerDatabase,
    ): ResultDao = database.resultDao()

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
    fun provideDataStorePreferences(@ApplicationContext appContext: Context): DataStorePreferences {
        return DataStorePreferences(appContext)
    }

    @Provides
    @Singleton
    fun provideWeightUnitService(dataStorePreferences: DataStorePreferences): WeightUnitService {
        return WeightUnitService(dataStorePreferences)
    }

    @Provides
    @Singleton
    fun provideAnalyticsService(dataStorePreferences: DataStorePreferences): AnalyticsService {
        return AnalyticsService(dataStorePreferences)
    }

    @Provides
    @Singleton
    fun provideMovementsRepository(db: OneRepMaxTrackerDatabase): MovementsRepository {
        return DefaultMovementsRepository(db.movementDao(), db.resultDao())
    }

    @Provides
    @Singleton
    fun provideOneRepMaxRepository(
        db: OneRepMaxTrackerDatabase,
        weightUnitService: WeightUnitService,
        analyticsService: AnalyticsService,
    ): ResultRepository {
        return DefaultResultRepository(db.resultDao(), weightUnitService, analyticsService)
    }
}

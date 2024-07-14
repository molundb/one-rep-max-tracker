package net.martinlundberg.onerepmaxtracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.AnalyticsServiceImpl
import net.martinlundberg.onerepmaxtracker.data.database.OneRepMaxTrackerDatabase
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultMovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultResultRepository
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovementsRepository(db: OneRepMaxTrackerDatabase): MovementsRepository {
        return DefaultMovementsRepository(db.movementDao(), db.resultDao())
    }

    @Provides
    @Singleton
    fun provideOneRepMaxRepository(
        db: OneRepMaxTrackerDatabase,
        weightUnitService: WeightUnitServiceImpl,
        analyticsService: AnalyticsServiceImpl,
    ): ResultRepository {
        return DefaultResultRepository(db.resultDao(), weightUnitService, analyticsService)
    }
}
package net.martinlundberg.onerepmaxtracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.data.database.OneRepMaxTrackerDatabase
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultMovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultResultRepository
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenRepository
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovementsRepository(
        db: OneRepMaxTrackerDatabase,
        analyticsService: AnalyticsService,
        latestOrBestResultsInMovementListScreenRepository: LatestOrBestResultsInMovementListScreenRepository,
    ): MovementsRepository {
        return DefaultMovementsRepository(
            db.movementDao(),
            db.resultDao(),
            analyticsService,
            latestOrBestResultsInMovementListScreenRepository,
        )
    }

    @Provides
    @Singleton
    fun provideOneRepMaxRepository(
        db: OneRepMaxTrackerDatabase,
        weightUnitService: DefaultWeightUnitRepository,
        analyticsService: AnalyticsService,
    ): ResultRepository {
        return DefaultResultRepository(
            db.resultDao(),
            weightUnitService,
            analyticsService,
        )
    }
}
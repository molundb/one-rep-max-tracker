package net.martinlundberg.onerepmaxtracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.DefaultAnalyticsEnabledService
import net.martinlundberg.onerepmaxtracker.data.database.OneRepMaxTrackerDatabase
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultMovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.DefaultResultRepository
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenService
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovementsRepository(
        db: OneRepMaxTrackerDatabase,
        analyticsService: AnalyticsService,
        latestOrBestResultsInMovementListScreenService: LatestOrBestResultsInMovementListScreenService,
    ): MovementsRepository {
        return DefaultMovementsRepository(
            db.movementDao(),
            db.resultDao(),
            analyticsService,
            latestOrBestResultsInMovementListScreenService,
        )
    }

    @Provides
    @Singleton
    fun provideOneRepMaxRepository(
        db: OneRepMaxTrackerDatabase,
        weightUnitService: DefaultWeightUnitService,
        analyticsEnabledService: DefaultAnalyticsEnabledService,
        analyticsService: AnalyticsService,
    ): ResultRepository {
        return DefaultResultRepository(
            db.resultDao(),
            weightUnitService,
            analyticsEnabledService,
            analyticsService,
        )
    }
}
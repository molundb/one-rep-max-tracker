package net.martinlundberg.onerepmaxtracker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.ClockService
import net.martinlundberg.onerepmaxtracker.DefaultClockService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledRepository
import net.martinlundberg.onerepmaxtracker.analytics.DefaultAnalyticsEnabledRepository
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenRepository
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenRepositoryImpl
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository
import net.martinlundberg.onerepmaxtracker.util.WeightUnitRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun provideWeightUnitRepository(weightUnitRepository: DefaultWeightUnitRepository): WeightUnitRepository

    @Binds
    @Singleton
    abstract fun provideAnalyticsRepository(analyticsEnabledRepository: DefaultAnalyticsEnabledRepository): AnalyticsEnabledRepository

    @Binds
    @Singleton
    abstract fun provideClockService(clockServiceImpl: DefaultClockService): ClockService

    @Binds
    @Singleton
    abstract fun provideLatestOrBestResultsInMovementListScreenRepository(
        latestOrBestResultsInMovementListScreenRepository: LatestOrBestResultsInMovementListScreenRepositoryImpl,
    ): LatestOrBestResultsInMovementListScreenRepository
}
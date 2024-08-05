package net.martinlundberg.onerepmaxtracker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.ClockService
import net.martinlundberg.onerepmaxtracker.DefaultClockService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledService
import net.martinlundberg.onerepmaxtracker.analytics.DefaultAnalyticsEnabledService
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenService
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenServiceImpl
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService
import net.martinlundberg.onerepmaxtracker.util.WeightUnitService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun provideWeightUnitService(weightUnitService: DefaultWeightUnitService): WeightUnitService

    @Binds
    @Singleton
    abstract fun provideAnalyticsService(analyticsService: DefaultAnalyticsEnabledService): AnalyticsEnabledService

    @Binds
    @Singleton
    abstract fun provideClockService(clockServiceImpl: DefaultClockService): ClockService

    @Binds
    @Singleton
    abstract fun provideLatestOrBestResultsInMovementListScreenService(latestOrBestResultsInMovementListScreenService: LatestOrBestResultsInMovementListScreenServiceImpl): LatestOrBestResultsInMovementListScreenService
}
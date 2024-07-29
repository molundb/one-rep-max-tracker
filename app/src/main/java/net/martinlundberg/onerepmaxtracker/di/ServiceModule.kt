package net.martinlundberg.onerepmaxtracker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.ClockService
import net.martinlundberg.onerepmaxtracker.ClockServiceImpl
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledServiceImpl
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenService
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenServiceImpl
import net.martinlundberg.onerepmaxtracker.util.WeightUnitService
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun provideWeightUnitService(weightUnitService: WeightUnitServiceImpl): WeightUnitService

    @Binds
    @Singleton
    abstract fun provideAnalyticsService(analyticsService: AnalyticsEnabledServiceImpl): AnalyticsEnabledService

    @Binds
    @Singleton
    abstract fun provideClockService(clockServiceImpl: ClockServiceImpl): ClockService

    @Binds
    @Singleton
    abstract fun provideLatestOrBestResultsInMovementListScreenService(latestOrBestResultsInMovementListScreenService: LatestOrBestResultsInMovementListScreenServiceImpl): LatestOrBestResultsInMovementListScreenService
}
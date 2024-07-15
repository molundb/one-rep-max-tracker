package net.martinlundberg.onerepmaxtracker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.ClockService
import net.martinlundberg.onerepmaxtracker.ClockServiceImpl
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsServiceImpl
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
    abstract fun provideAnalyticsService(analyticsService: AnalyticsServiceImpl): AnalyticsService

    @Binds
    @Singleton
    abstract fun provideClockService(clockServiceImpl: ClockServiceImpl): ClockService
}
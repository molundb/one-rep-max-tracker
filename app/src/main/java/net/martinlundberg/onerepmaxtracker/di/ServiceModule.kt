package net.martinlundberg.onerepmaxtracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.martinlundberg.onerepmaxtracker.AnalyticsService
import net.martinlundberg.onerepmaxtracker.ClockService
import net.martinlundberg.onerepmaxtracker.ClockServiceImpl
import net.martinlundberg.onerepmaxtracker.data.DataStorePreferences
import net.martinlundberg.onerepmaxtracker.util.WeightUnitService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    // TODO: Change to @Binds?
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
    fun provideClockService(): ClockService {
        return ClockServiceImpl()
    }
}
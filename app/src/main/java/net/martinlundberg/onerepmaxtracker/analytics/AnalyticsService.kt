package net.martinlundberg.onerepmaxtracker.analytics

interface AnalyticsService {
    fun logEvent(event: AnalyticsEvent)
    fun setWeightUnitAsUserProperty(weightUnit: String)
}
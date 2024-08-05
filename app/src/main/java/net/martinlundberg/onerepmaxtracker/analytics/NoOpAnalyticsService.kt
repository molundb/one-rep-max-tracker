package net.martinlundberg.onerepmaxtracker.analytics

/**
 * Implementation of AnalyticsService which does nothing. Useful for tests and previews.
 */
class NoOpAnalyticsService : AnalyticsService {
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun setWeightUnitAsUserProperty(weightUnit: String) = Unit
}
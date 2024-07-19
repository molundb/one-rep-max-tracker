package net.martinlundberg.onerepmaxtracker.analytics

/**
 * Implementation of AnalyticsHelper which does nothing. Useful for tests and previews.
 */
class NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun setWeightUnitAsUserProperty(weightUnit: String) = Unit
}
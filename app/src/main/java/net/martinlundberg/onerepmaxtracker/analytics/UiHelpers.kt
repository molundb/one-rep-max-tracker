package net.martinlundberg.onerepmaxtracker.analytics

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Global key used to obtain access to the AnalyticsService through a CompositionLocal.
 */
val LocalAnalyticsService = staticCompositionLocalOf<AnalyticsService> {
    // Provide a default AnalyticsService which does nothing. This is so that tests and previews
    // do not have to provide one. For real app builds provide a different implementation.
    NoOpAnalyticsService()
}

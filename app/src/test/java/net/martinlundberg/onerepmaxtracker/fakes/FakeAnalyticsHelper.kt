package net.martinlundberg.onerepmaxtracker.fakes

import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper

class FakeAnalyticsHelper : AnalyticsHelper {

    private val events = mutableListOf<AnalyticsEvent>()
    override fun logEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    override fun setWeightUnitAsUserProperty(weightUnit: String) {
        TODO("Not yet implemented")
    }

    fun hasLogged(event: AnalyticsEvent) = event in events
    fun numberOfEvents() = events.size
}
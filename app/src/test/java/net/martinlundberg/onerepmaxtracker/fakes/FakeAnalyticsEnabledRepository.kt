package net.martinlundberg.onerepmaxtracker.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledRepository

class FakeAnalyticsEnabledRepository : AnalyticsEnabledRepository {
    private val _analyticsCollectionEnabled = MutableStateFlow(false)

    override val analyticsEnabledFlow: StateFlow<Boolean>
        get() = _analyticsCollectionEnabled

    override suspend fun setAnalyticsCollectionEnabled(isEnabled: Boolean) {
        _analyticsCollectionEnabled.value = isEnabled
    }
}
package net.martinlundberg.onerepmaxtracker

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.data.DataStorePreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsService @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
) {
    private val scope = MainScope()

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
    private val firebaseCrashlytics: FirebaseCrashlytics = Firebase.crashlytics

    private val _analyticsEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val analyticsEnabledFlow: StateFlow<Boolean> = _analyticsEnabled.asStateFlow()

    init {
        scope.launch {
            collectAnalyticsEnabledState()
        }
    }

    private suspend fun collectAnalyticsEnabledState() {
        dataStorePreferences.analyticsCollectionEnabledFlow.collect {
            firebaseAnalytics.setAnalyticsCollectionEnabled(it)
            firebaseCrashlytics.setCrashlyticsCollectionEnabled(it)
            _analyticsEnabled.value = it
        }
    }

    suspend fun setAnalyticsCollectionEnabled(isEnabled: Boolean) =
        dataStorePreferences.storeAnalyticsCollectionEnabled(isEnabled)
}
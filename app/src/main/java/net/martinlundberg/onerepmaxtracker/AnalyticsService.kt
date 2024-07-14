package net.martinlundberg.onerepmaxtracker

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.ConsentStatus
import com.google.firebase.analytics.FirebaseAnalytics.ConsentStatus.DENIED
import com.google.firebase.analytics.FirebaseAnalytics.ConsentStatus.GRANTED
import com.google.firebase.analytics.FirebaseAnalytics.ConsentType
import com.google.firebase.analytics.FirebaseAnalytics.ConsentType.AD_PERSONALIZATION
import com.google.firebase.analytics.FirebaseAnalytics.ConsentType.AD_STORAGE
import com.google.firebase.analytics.FirebaseAnalytics.ConsentType.AD_USER_DATA
import com.google.firebase.analytics.FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE
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
import java.util.EnumMap
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
        dataStorePreferences.analyticsCollectionEnabledFlow.collect { isEnabled ->
            firebaseAnalytics.setAnalyticsCollectionEnabled(isEnabled)
            firebaseCrashlytics.setCrashlyticsCollectionEnabled(isEnabled)

            // The kotlin way is not working for some reason
            // Firebase.analytics.setConsent {
            //     analyticsStorage(ConsentStatus.GRANTED)
            //     adStorage(ConsentStatus.GRANTED)
            //     adUserData(ConsentStatus.GRANTED)
            //     adPersonalization(ConsentStatus.GRANTED)
            // }
            val consentMap: MutableMap<ConsentType, ConsentStatus> = EnumMap(
                ConsentType::class.java
            )
            if (isEnabled) {
                consentMap[ANALYTICS_STORAGE] = GRANTED
                consentMap[AD_STORAGE] = GRANTED
                consentMap[AD_USER_DATA] = GRANTED
                consentMap[AD_PERSONALIZATION] = GRANTED
            } else {
                consentMap[ANALYTICS_STORAGE] = DENIED
                consentMap[AD_STORAGE] = DENIED
                consentMap[AD_USER_DATA] = DENIED
                consentMap[AD_PERSONALIZATION] = DENIED
            }
            firebaseAnalytics.setConsent(consentMap)

            _analyticsEnabled.value = isEnabled
        }
    }

    suspend fun setAnalyticsCollectionEnabled(isEnabled: Boolean) =
        dataStorePreferences.storeAnalyticsCollectionEnabled(isEnabled)
}
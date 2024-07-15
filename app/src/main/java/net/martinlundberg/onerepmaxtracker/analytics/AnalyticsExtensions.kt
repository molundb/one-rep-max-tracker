package net.martinlundberg.onerepmaxtracker.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.Param
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.ParamKeys
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.Types
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit

fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = listOf(
                Param(ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

fun AnalyticsHelper.logWeightUnitToggled(weightUnit: WeightUnit) {
    val type = "weight_unit_toggled"
    val paramKey = "weight_unit"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKey, value = weightUnit.toString())
            )
        )
    )
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
//    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
    analyticsHelper: AnalyticsHelper,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}
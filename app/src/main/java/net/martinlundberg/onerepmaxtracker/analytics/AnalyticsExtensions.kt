package net.martinlundberg.onerepmaxtracker.analytics

import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.Param
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit

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
package net.martinlundberg.onerepmaxtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.logWeightUnitToggled
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val weightUnitService: DefaultWeightUnitRepository,
    private val analyticsService: AnalyticsService,
) : ViewModel() {
    val weightUnitFlow: StateFlow<WeightUnit> = weightUnitService.weightUnitFlow

    fun setWeightUnit(isPounds: Boolean) {
        viewModelScope.launch {
            weightUnitService.setWeightUnit(isPounds)
        }

        val weightUnit = if (isPounds) WeightUnit.POUNDS else WeightUnit.KILOGRAMS
        analyticsService.logWeightUnitToggled(weightUnit)
    }

    fun setWeightUnitAsUserProperty(weightUnit: WeightUnit) =
        analyticsService.setWeightUnitAsUserProperty(weightUnit.toString())
}
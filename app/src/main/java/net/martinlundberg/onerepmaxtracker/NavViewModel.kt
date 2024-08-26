package net.martinlundberg.onerepmaxtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.logWeightUnitToggled
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitRepository
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val weightUnitRepository: WeightUnitRepository,
    private val analyticsService: AnalyticsService,
) : ViewModel() {
    val weightUnitFlow: StateFlow<WeightUnit> = weightUnitRepository.weightUnitFlow

    fun setWeightUnit(isPounds: Boolean) {
        viewModelScope.launch {
            weightUnitRepository.setWeightUnit(isPounds)
        }

        val weightUnit = if (isPounds) WeightUnit.POUNDS else WeightUnit.KILOGRAMS
        analyticsService.logWeightUnitToggled(weightUnit)
    }

    fun setWeightUnitAsUserProperty(weightUnit: WeightUnit) =
        analyticsService.setWeightUnitAsUserProperty(weightUnit.toString())
}
package net.martinlundberg.onerepmaxtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logWeightUnitToggled
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    navigationService: NavigationService,
    private val weightUnitService: WeightUnitServiceImpl,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    val controller = navigationService.navController

    val weightUnitFlow: StateFlow<WeightUnit> = weightUnitService.weightUnitFlow

    fun setWeightUnit(isPounds: Boolean) {
        viewModelScope.launch {
            weightUnitService.setWeightUnit(isPounds)
        }

        analyticsHelper.logWeightUnitToggled(if (isPounds) WeightUnit.POUNDS else WeightUnit.KILOGRAMS)
    }
}
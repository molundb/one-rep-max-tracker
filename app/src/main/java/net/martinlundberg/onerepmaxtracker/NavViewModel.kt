package net.martinlundberg.onerepmaxtracker

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logMovementList_MovementClick
import net.martinlundberg.onerepmaxtracker.analytics.logWeightUnitToggled
import net.martinlundberg.onerepmaxtracker.ui.model.MovementUiModel
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

        val weightUnit = if (isPounds) WeightUnit.POUNDS else WeightUnit.KILOGRAMS
        analyticsHelper.logWeightUnitToggled(weightUnit)
    }

    fun setWeightUnitAsUserProperty(weightUnit: WeightUnit) =
        analyticsHelper.setWeightUnitAsUserProperty(weightUnit.toString())

    fun navigateToDetail(movement: MovementUiModel, lifeCycleState: Lifecycle.State, route: String) {
        analyticsHelper.logMovementList_MovementClick(movement)
        if (lifeCycleState.isAtLeast(Lifecycle.State.RESUMED)) {
            controller.navigate(route)
        }
    }
}
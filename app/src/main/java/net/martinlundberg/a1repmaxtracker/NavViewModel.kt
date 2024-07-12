package net.martinlundberg.a1repmaxtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    navigationService: NavigationService,
    private val weightUnitService: WeightUnitService,
) : ViewModel() {
    val controller = navigationService.navController

    val weightUnitFlow: StateFlow<WeightUnit> = weightUnitService.weightUnitFlow

    fun setWeightUnit(isPounds: Boolean) {
        viewModelScope.launch {
            weightUnitService.setWeightUnit(isPounds)
        }
    }
}
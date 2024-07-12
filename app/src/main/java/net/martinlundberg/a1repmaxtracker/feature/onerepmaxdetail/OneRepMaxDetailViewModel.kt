package net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.NavigationService
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.repository.OneRepMaxRepository
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Success
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
import javax.inject.Inject

@HiltViewModel
class OneRepMaxDetailViewModel @Inject constructor(
    private val oneRepMaxRepository: OneRepMaxRepository,
    private val navigationService: NavigationService,
) : ViewModel() {
    private val _uiState: MutableStateFlow<OneRepMaxDetailUiState> = MutableStateFlow(Loading(WeightUnit.KILOGRAMS))
    val uiState: StateFlow<OneRepMaxDetailUiState> = _uiState.asStateFlow()

    fun getOneRepMaxDetail(id: Long) {
        viewModelScope.launch {
            combine(
                oneRepMaxRepository.getOneRM(id),
                oneRepMaxRepository.getWeightUnitFlow(),
            ) { oneRMInfo, weightUnit ->
                _uiState.update { Success(oneRMInfo, weightUnit) }
            }
        }
    }

    fun updateOneRepMaxDetail(oneRMInfo: OneRMInfo) {
        viewModelScope.launch {
            oneRepMaxRepository.addOneRM(oneRMInfo, WeightUnit.KILOGRAMS)
        }
    }

    fun deleteOneRM(id: Long) {
        viewModelScope.launch {
            oneRepMaxRepository.deleteOneRM(id)
            navigationService.navController.popBackStack()
        }
    }

    fun setWeightUnit(isPounds: Boolean) {
        viewModelScope.launch {
            oneRepMaxRepository.setWeightUnit(isPounds)
        }
    }
}

sealed interface OneRepMaxDetailUiState {
    val weightUnit: WeightUnit

    data class Loading(
        override val weightUnit: WeightUnit,
    ) : OneRepMaxDetailUiState

    data class Success(
        val oneRMInfo: OneRMInfo,
        override val weightUnit: WeightUnit,
    ) : OneRepMaxDetailUiState
}
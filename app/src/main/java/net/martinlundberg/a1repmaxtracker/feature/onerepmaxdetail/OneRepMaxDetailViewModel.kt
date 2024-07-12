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
import net.martinlundberg.a1repmaxtracker.data.model.Result
import net.martinlundberg.a1repmaxtracker.data.repository.ResultRepository
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Success
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
import javax.inject.Inject

@HiltViewModel
class OneRepMaxDetailViewModel @Inject constructor(
    private val resultRepository: ResultRepository,
    private val navigationService: NavigationService,
) : ViewModel() {
    private val _uiState: MutableStateFlow<OneRepMaxDetailUiState> = MutableStateFlow(Loading(WeightUnit.KILOGRAMS))
    val uiState: StateFlow<OneRepMaxDetailUiState> = _uiState.asStateFlow()

    fun getResult(id: Long) {
        viewModelScope.launch {
            combine(
                resultRepository.getResult(id),
                resultRepository.getWeightUnitFlow(),
            ) { result, weightUnit ->
                _uiState.update { Success(result, weightUnit) }
            }
        }
    }

    fun updateResult(result: Result) {
        viewModelScope.launch {
            resultRepository.addResult(result, WeightUnit.KILOGRAMS)
        }
    }

    fun deleteResult(id: Long) {
        viewModelScope.launch {
            resultRepository.deleteResult(id)
            navigationService.navController.popBackStack()
        }
    }

    fun setWeightUnit(isPounds: Boolean) {
        viewModelScope.launch {
            resultRepository.setWeightUnit(isPounds)
        }
    }
}

sealed interface OneRepMaxDetailUiState {
    val weightUnit: WeightUnit

    data class Loading(
        override val weightUnit: WeightUnit,
    ) : OneRepMaxDetailUiState

    data class Success(
        val result: Result,
        override val weightUnit: WeightUnit,
    ) : OneRepMaxDetailUiState
}
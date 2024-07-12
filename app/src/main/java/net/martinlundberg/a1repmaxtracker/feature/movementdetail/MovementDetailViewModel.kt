package net.martinlundberg.a1repmaxtracker.feature.movementdetail

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
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.a1repmaxtracker.data.repository.OneRepMaxRepository
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
import javax.inject.Inject

@HiltViewModel
class MovementDetailViewModel @Inject constructor(
    private val movementsRepository: MovementsRepository,
    private val oneRepMaxRepository: OneRepMaxRepository,
    private val navigationService: NavigationService,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementDetailUiState> = _uiState.asStateFlow()

    fun getMovementInfo(id: Long) {
        viewModelScope.launch {
            combine(
                oneRepMaxRepository.getMovementDetail(id),
                oneRepMaxRepository.getWeightUnitFlow(),
            ) { oneRMInfo, weightUnit ->
                Success(oneRMInfo, weightUnit)
            }.collect { newState ->
                _uiState.update { newState }
            }
        }
    }

    fun addResult(oneRMInfo: OneRMInfo, weightUnit: WeightUnit) {
        viewModelScope.launch {
            oneRepMaxRepository.addOneRM(
                OneRMInfo(
                    id = oneRMInfo.id,
                    weight = oneRMInfo.weight,
                    offsetDateTime = oneRMInfo.offsetDateTime,
                    movementId = oneRMInfo.movementId,
                ),
                weightUnit = weightUnit,
            )
        }
    }

    fun deleteMovement(id: Long) {
        viewModelScope.launch {
            movementsRepository.deleteMovement(id)
            navigationService.navController.popBackStack()
        }
    }

    fun deleteResult(id: Long) {
        viewModelScope.launch {
            oneRepMaxRepository.deleteOneRM(id)
        }
    }
}

sealed interface MovementDetailUiState {
    data object Loading : MovementDetailUiState

    data class Success(
        val movement: MovementDetail,
        val weightUnit: WeightUnit,
    ) : MovementDetailUiState
}
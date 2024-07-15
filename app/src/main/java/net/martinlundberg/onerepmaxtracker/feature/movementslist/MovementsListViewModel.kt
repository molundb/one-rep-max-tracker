package net.martinlundberg.onerepmaxtracker.feature.movementslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementslist.MovementsListUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MovementsListViewModel @Inject constructor(
    private val movementsRepository: MovementsRepository,
    private val resultRepository: ResultRepository,
    val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementsListUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementsListUiState> = _uiState.asStateFlow()

    fun getMovements() {
        viewModelScope.launch {
            combine(
                movementsRepository.getMovements(),
                resultRepository.getWeightUnitFlow(),
                resultRepository.getAnalyticsCollectionEnabledFlow(),
            ) { movements, weightUnit, isAnalyticsEnabled ->
                Success(movements, weightUnit, isAnalyticsEnabled)
            }.collect { newState ->
                _uiState.update { newState }
            }
        }
    }

    fun addMovement(movement: Movement, weightUnit: WeightUnit) {
        viewModelScope.launch {
            val movementId = movementsRepository.setMovement(movement.copy(name = movement.name.trim()))
            movement.weight?.let {
                resultRepository.addResult(
                    Result(
                        weight = it,
                        offsetDateTime = OffsetDateTime.now(),
                        movementId = movementId,
                    ),
                    weightUnit = weightUnit,
                )
            }
        }
    }

    fun editMovement(movement: Movement) {
        viewModelScope.launch {
            movementsRepository.setMovement(movement)
        }
    }

    fun deleteMovement(id: Long) {
        viewModelScope.launch {
            movementsRepository.deleteMovement(id)
        }
    }

    fun setAnalyticsCollectionEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            resultRepository.setAnalyticsCollectionEnabled(isEnabled)
        }
    }
}

sealed interface MovementsListUiState {
    data object Loading : MovementsListUiState

    data class Success(
        val movements: List<Movement> = emptyList(),
        val weightUnit: WeightUnit,
        val isAnalyticsEnabled: Boolean,
    ) : MovementsListUiState
}

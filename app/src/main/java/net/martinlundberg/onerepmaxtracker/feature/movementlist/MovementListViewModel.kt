package net.martinlundberg.onerepmaxtracker.feature.movementlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.ClockService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovement
import net.martinlundberg.onerepmaxtracker.analytics.logAddResult
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovement
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.millisToOffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MovementListViewModel @Inject constructor(
    private val movementsRepository: MovementsRepository,
    private val resultRepository: ResultRepository,
    private val clockService: ClockService,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementListUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementListUiState> = _uiState.asStateFlow()

    fun getMovements() {
        viewModelScope.launch {
            combine(
                movementsRepository.movements,
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
        analyticsHelper.logAddMovement(movement)
        viewModelScope.launch {
            val movementId = movementsRepository.setMovement(movement.copy(name = movement.name.trim()))
            movement.weight?.let {
                val result = Result(
                    weight = it,
                    offsetDateTime = clockService
                        .getCurrentTimeMillis()
                        .millisToOffsetDateTime(ZoneId.systemDefault()),
                    movementId = movementId,
                    comment = "",
                )
                analyticsHelper.logAddResult(result)
                resultRepository.setResult(
                    result,
                    weightUnit = weightUnit,
                )
            }
        }
    }

    fun editMovement(movement: Movement) {
        analyticsHelper.logEditMovement(movement)
        viewModelScope.launch {
            movementsRepository.setMovement(movement)
        }
    }

    fun deleteMovement(movementId: Long) {
        viewModelScope.launch {
            movementsRepository.deleteMovement(movementId)
        }
    }

    fun setAnalyticsCollectionEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            resultRepository.setAnalyticsCollectionEnabled(isEnabled)
        }
    }
}

sealed interface MovementListUiState {
    data object Loading : MovementListUiState

    data class Success(
        val movements: List<Movement> = emptyList(),
        val weightUnit: WeightUnit,
        val isAnalyticsEnabled: Boolean,
    ) : MovementListUiState
}

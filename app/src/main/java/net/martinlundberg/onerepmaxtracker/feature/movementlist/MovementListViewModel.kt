package net.martinlundberg.onerepmaxtracker.feature.movementlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.ClockService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovement
import net.martinlundberg.onerepmaxtracker.analytics.logAddResult
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovement
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults.BEST
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults.LATEST
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.millisToOffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MovementListViewModel @Inject constructor(
    private val movementsRepository: MovementsRepository,
    private val resultRepository: ResultRepository,
    private val clockService: ClockService,
    private val analyticsService: AnalyticsService,
) : ViewModel() {
    val uiState: StateFlow<MovementListUiState> = combine(
        movementsRepository.movements,
        resultRepository.getWeightUnitFlow(),
        resultRepository.getAnalyticsCollectionEnabledFlow(),
        movementsRepository.latestOrBestResults,
    ) { movements, weightUnit, isAnalyticsEnabled, latestOrBestResults ->
        Success(
            movements = movements,
            weightUnit = weightUnit,
            isAnalyticsEnabled = isAnalyticsEnabled,
            latestOrBestResults == BEST,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading,
    )

    fun addMovement(movement: Movement, weightUnit: WeightUnit) {
        viewModelScope.launch {
            analyticsService.logAddMovement(movement)
            val movementId = movementsRepository.setMovement(movement.copy(name = movement.name.trim()))
            movement.weight?.let {
                val result = Result(
                    weight = it,
                    movementId = movementId,
                    offsetDateTime = clockService
                        .getCurrentTimeMillis()
                        .millisToOffsetDateTime(ZoneId.systemDefault()),
                    comment = "",
                )
                analyticsService.logAddResult(result)
                resultRepository.setResult(
                    result,
                    weightUnit = weightUnit,
                )
            }
        }
    }

    fun editMovement(movement: Movement) {
        viewModelScope.launch {
            analyticsService.logEditMovement(movement)
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

    fun showLatestOrBestResults(showBest: Boolean) {
        viewModelScope.launch {
            movementsRepository.setLatestOrBestResults(if (showBest) BEST else LATEST)
        }
    }
}

sealed interface MovementListUiState {
    data object Loading : MovementListUiState

    data class Success(
        val movements: List<Movement> = emptyList(),
        val weightUnit: WeightUnit,
        val isAnalyticsEnabled: Boolean,
        val showBestResults: Boolean,
    ) : MovementListUiState
}

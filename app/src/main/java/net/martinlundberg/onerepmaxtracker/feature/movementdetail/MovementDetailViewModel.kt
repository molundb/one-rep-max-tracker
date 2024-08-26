package net.martinlundberg.onerepmaxtracker.feature.movementdetail

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
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.logAddResult
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovement
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.NoMovementDetail
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.millisToOffsetDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MovementDetailViewModel @Inject constructor(
    private val movementsRepository: MovementsRepository,
    private val resultRepository: ResultRepository,
    private val clockService: ClockService,
    private val analyticsService: AnalyticsService,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementDetailUiState> = MutableStateFlow(Loading())
    val uiState: StateFlow<MovementDetailUiState> = _uiState.asStateFlow()

    fun getMovementInfo(id: Long) {
        viewModelScope.launch {
            combine(
                resultRepository.getMovementDetail(id),
                resultRepository.getWeightUnitFlow(),
            ) { movementDetail, weightUnit ->
                if (movementDetail == null) {
                    NoMovementDetail()
                } else {
                    Success(
                        movementDetail.apply { formatResultDates(clockService.getCurrentTimeMillis()) },
                        weightUnit,
                        clockService.getCurrentTimeMillis().millisToOffsetDateTime(ZoneId.systemDefault()),
                    )
                }
            }.collect { newState ->
                _uiState.update { newState }
            }
        }
    }

    fun addResult(result: Result, weightUnit: WeightUnit) {
        analyticsService.logAddResult(result)
        viewModelScope.launch {
            resultRepository.setResult(
                result = result,
                weightUnit = weightUnit,
            )
        }
    }

    fun editMovement(movement: Movement) {
        analyticsService.logEditMovement(movement)
        viewModelScope.launch {
            movementsRepository.setMovement(movement)
        }
    }

    fun deleteMovement(id: Long) {
        viewModelScope.launch {
            movementsRepository.deleteMovement(id)
        }
    }

    fun deleteResult(id: Long) {
        viewModelScope.launch {
            resultRepository.deleteResult(id)
        }
    }
}

sealed interface MovementDetailUiState {
    val movement: MovementDetail?

    data class Loading(
        override val movement: MovementDetail? = null,
    ) : MovementDetailUiState

    data class Success(
        override val movement: MovementDetail,
        val weightUnit: WeightUnit,
        val currentOffsetDateTime: OffsetDateTime,
    ) : MovementDetailUiState

    data class NoMovementDetail(
        override val movement: MovementDetail? = null,
    ) : MovementDetailUiState
}
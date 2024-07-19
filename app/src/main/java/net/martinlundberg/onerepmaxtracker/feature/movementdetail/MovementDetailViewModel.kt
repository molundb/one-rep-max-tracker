package net.martinlundberg.onerepmaxtracker.feature.movementdetail

import android.text.format.DateUtils
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
import net.martinlundberg.onerepmaxtracker.NavigationService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovementConfirmDialog_DeleteClick
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteResult
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovement
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MovementDetailViewModel @Inject constructor(
    private val movementsRepository: MovementsRepository,
    private val resultRepository: ResultRepository,
    private val navigationService: NavigationService,
    private val clockService: ClockService,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementDetailUiState> = MutableStateFlow(Loading(MovementDetail("")))
    val uiState: StateFlow<MovementDetailUiState> = _uiState.asStateFlow()

    fun getMovementInfo(id: Long) {
        viewModelScope.launch {
            combine(
                resultRepository.getMovementDetail(id),
                resultRepository.getWeightUnitFlow(),
            ) { result, weightUnit ->
                Success(result, weightUnit)
            }.collect { newState ->
                val sortedByDate = newState.movement.results.sortedByDescending { it.offsetDateTime }

                _uiState.update {
                    newState.copy(
                        movement = MovementDetail(
                            newState.movement.movementName,
                            sortedByDate
                        )
                    )
                }
            }
        }
    }

    fun addResult(result: Result, weightUnit: WeightUnit) {
        viewModelScope.launch {
            resultRepository.addResult(
                Result(
                    id = result.id,
                    weight = result.weight,
                    offsetDateTime = result.offsetDateTime,
                    movementId = result.movementId,
                ),
                weightUnit = weightUnit,
            )
        }
    }

    fun editMovement(movement: Movement) {
        analyticsHelper.logEditMovement(movement)
        viewModelScope.launch {
            movementsRepository.setMovement(movement)
        }
    }

    fun deleteMovement(id: Long) {
        // TODO: Move to repo and rename?
        analyticsHelper.logDeleteMovementConfirmDialog_DeleteClick(id)
        viewModelScope.launch {
            movementsRepository.deleteMovement(id)
            navigationService.navController.popBackStack()
        }
    }

    fun deleteResult(id: Long) {
        // TODO: Move to repo and rename?
        analyticsHelper.logDeleteResult(id)
        viewModelScope.launch {
            resultRepository.deleteResult(id)
        }
    }

    fun getRelativeDateString(offsetDateTime: OffsetDateTime) =
        DateUtils.getRelativeTimeSpanString(
            offsetDateTime.toInstant().toEpochMilli(),
            clockService.getCurrentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
        ).toString()
}

sealed interface MovementDetailUiState {
    val movement: MovementDetail

    data class Loading(
        override val movement: MovementDetail,
    ) : MovementDetailUiState

    data class Success(
        override val movement: MovementDetail,
        val weightUnit: WeightUnit,
    ) : MovementDetailUiState
}
package net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail

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
import net.martinlundberg.onerepmaxtracker.analytics.logEditResult
import net.martinlundberg.onerepmaxtracker.data.model.Percentage
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.getRelativeDateString
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ResultDetailViewModel @Inject constructor(
    private val resultRepository: ResultRepository,
    private val navigationService: NavigationService,
    private val clockService: ClockService,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ResultDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<ResultDetailUiState> = _uiState.asStateFlow()

    fun getResult(id: Long) {
        viewModelScope.launch {
            combine(
                resultRepository.getResult(id),
                resultRepository.getWeightUnitFlow(),
            ) { result, weightUnit ->
                val percentages = mutableListOf<Percentage>()
                for (percentage in 100 downTo 40 step 10) {
                    percentages.add(
                        Percentage(
                            percentage,
                            (result.weight * percentage / 100).roundToInt()
                        )
                    )
                }

                Success(result, percentages, weightUnit)
            }.collect { newState ->
                val stateWithFormattedDate = newState.copy(
                    result = newState.result.copy(
                        dateTimeFormatted = newState.result.offsetDateTime.getRelativeDateString(clockService.getCurrentTimeMillis()),
                    )
                )
                _uiState.update { stateWithFormattedDate }
            }
        }
    }

    fun editResult(result: Result, weightUnit: WeightUnit) {
        analyticsHelper.logEditResult(result)
        viewModelScope.launch {
            resultRepository.setResult(result, weightUnit)
        }
    }

    fun deleteResult(id: Long) {
        viewModelScope.launch {
            resultRepository.deleteResult(id)
            navigationService.navController.popBackStack()
        }
    }
}

sealed interface ResultDetailUiState {
    data object Loading : ResultDetailUiState

    data class Success(
        val result: Result,
        val percentagesOf1RM: List<Percentage>,
        val weightUnit: WeightUnit,
    ) : ResultDetailUiState
}
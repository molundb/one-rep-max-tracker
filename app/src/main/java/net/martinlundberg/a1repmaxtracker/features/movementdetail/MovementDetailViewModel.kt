package net.martinlundberg.a1repmaxtracker.features.movementdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.features.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.features.movementdetail.MovementDetailUiState.Success
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MovementDetailViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<MovementDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementDetailUiState> = _uiState.asStateFlow()

    fun getMovementInfo() {
        viewModelScope.launch {
            val backendResult = fetchMovementInfo()
            _uiState.update { backendResult }
        }
    }

    private suspend fun fetchMovementInfo(): Success {
        // Simulate network delay
        delay(2000)
        return Success(
            MovementDetail(
                oneRMs = listOf(
                    OneRMInfo(70, "09/01/2023"),
                    OneRMInfo(75, "05/01/2023"),
                    OneRMInfo(78, "01/01/2023"),
                )
            )
        )
    }

    fun add1RM(value: Int) {
        viewModelScope.launch {
            // Update backend with new 1RM
            val currentState = _uiState.value
            if (currentState is Success) {
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                val updated1RMs =
                    listOf(OneRMInfo(value, LocalDateTime.now().format(formatter))) + currentState.movement.oneRMs
                _uiState.value = Success(currentState.movement.copy(oneRMs = updated1RMs))
            }
        }
    }
}

sealed interface MovementDetailUiState {
    data object Loading : MovementDetailUiState

    data class Success(
        val movement: MovementDetail,
    ) : MovementDetailUiState
}

data class MovementDetail(
    val oneRMs: List<OneRMInfo> = emptyList(),
)

data class OneRMInfo(
    val weight: Int,
    val date: String,
)
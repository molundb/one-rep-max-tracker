package net.martinlundberg.a1repmaxtracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovementsListViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<MovementsListUiState> = MutableStateFlow(MovementsListUiState.Loading)
    val uiState: StateFlow<MovementsListUiState> = _uiState.asStateFlow()

    fun getMovements() {
        viewModelScope.launch {
            val backendResult = fetchMovements()
            _uiState.update { backendResult }
        }
    }

    // Suspend function to perform the backend call
    private suspend fun fetchMovements(): MovementsListUiState.Success {
        // Simulate network delay
        delay(2000)
        return MovementsListUiState.Success(
            listOf(
                Movement("Movement 1", 100),
                Movement("Movement 2", 6),
                Movement("Movement 3", 82),
                Movement("Movement 4", 33),
            )
        )
    }

    fun addMovement(newMovement: String) {
        viewModelScope.launch {
            // Update backend with new movement
            val currentState = _uiState.value
            if (currentState is MovementsListUiState.Success) {
                val updatedMovements = currentState.movements + Movement(newMovement, null)
                _uiState.value = MovementsListUiState.Success(updatedMovements)
            }
        }
    }
}

sealed interface MovementsListUiState {
    data object Loading : MovementsListUiState

    data class Success(
        val movements: List<Movement> = emptyList(),
    ) : MovementsListUiState
}

data class Movement(
    val name: String,
    val weight: Int?,
)
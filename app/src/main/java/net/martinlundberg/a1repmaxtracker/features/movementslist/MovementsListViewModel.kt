package net.martinlundberg.a1repmaxtracker.features.movementslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.features.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.features.movementslist.MovementsListUiState.Success

class MovementsListViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<MovementsListUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementsListUiState> = _uiState.asStateFlow()

    fun getMovements() {
        viewModelScope.launch {
            val backendResult = fetchMovements()
            _uiState.update { backendResult }
        }
    }

    private suspend fun fetchMovements(): Success {
        // Simulate network delay
        delay(2000)
        return Success(
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
            if (currentState is Success) {
                val updatedMovements = currentState.movements + Movement(newMovement, null)
                _uiState.value = Success(updatedMovements)
            }
        }
    }

    fun deleteMovement(name: String) {
        viewModelScope.launch {
            // Delete movement in backend
            val currentState = _uiState.value
            if (currentState is Success) {
                val updatedMovements = currentState.movements.filter { it.name != name }
                _uiState.value = Success(updatedMovements)
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
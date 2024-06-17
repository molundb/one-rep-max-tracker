package net.martinlundberg.a1repmaxtracker.feature.movementslist

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Success

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

    fun addMovement(movement: Movement) {
        viewModelScope.launch {
            // Update backend with new movement
            val currentState = _uiState.value
            if (currentState is Success) {
                val updatedMovements = currentState.movements + movement
                _uiState.value = Success(updatedMovements)
            }
        }
    }

//    fun editMovement(movement: Movement) {
//        // TODO: Do this properly with local storage
//        deleteMovement(prevName)
//        addMovement(movement)
//    }

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

@Parcelize
data class Movement(
//    val id: Int,
    val name: String,
    val weight: Int? = null,
) : Parcelable
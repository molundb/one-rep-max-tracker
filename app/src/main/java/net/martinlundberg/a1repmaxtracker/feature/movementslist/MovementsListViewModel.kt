package net.martinlundberg.a1repmaxtracker.feature.movementslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Success

class MovementsListViewModel(
    private val movementsRepository: MovementsRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementsListUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementsListUiState> = _uiState.asStateFlow()

    fun getMovements() {
        viewModelScope.launch {
            val movements = Success(movementsRepository.getMovements())
            _uiState.update { movements }
        }
    }

    fun addMovement(movement: Movement) {
        viewModelScope.launch {
            movementsRepository.addMovement(movement)
            getMovements()
        }
    }

//    fun editMovement(movement: Movement) {
//        // TODO: Do this properly with Room
//        deleteMovement(prevName)
//        addMovement(movement)
//    }

    fun deleteMovement(id: Int) {
        viewModelScope.launch {
            movementsRepository.deleteMovement(id)
            getMovements()
        }
    }
}

sealed interface MovementsListUiState {
    data object Loading : MovementsListUiState

    data class Success(
        val movements: List<Movement> = emptyList(),
    ) : MovementsListUiState
}

class MovementsListViewModelFactory(private val repository: MovementsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovementsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovementsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

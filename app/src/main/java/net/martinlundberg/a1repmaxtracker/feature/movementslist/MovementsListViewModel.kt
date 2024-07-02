package net.martinlundberg.a1repmaxtracker.feature.movementslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Success
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MovementsListViewModel @Inject constructor(
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
            val movementId = movementsRepository.setMovement(movement)
            movement.weight?.let {
                movementsRepository.addOneRM(OneRMInfo(weight = it, date = OffsetDateTime.now()), movementId)
            }
            getMovements()
        }
    }

    fun editMovement(movement: Movement) {
        viewModelScope.launch {
            movementsRepository.setMovement(movement)
            getMovements()
        }
    }

    fun deleteMovement(id: Long) {
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

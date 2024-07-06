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
import net.martinlundberg.a1repmaxtracker.data.repository.OneRepMaxRepository
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Success
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.poundsToKilos
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MovementsListViewModel @Inject constructor(
    private val movementsRepository: MovementsRepository,
    private val oneRepMaxRepository: OneRepMaxRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementsListUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementsListUiState> = _uiState.asStateFlow()

    fun getMovements() {
        viewModelScope.launch {
            movementsRepository.getMovements()
                .collect { movements ->
                    _uiState.update { Success(movements) }
                }
        }
    }

    fun addMovement(movement: Movement, weightUnit: String) {
        viewModelScope.launch {
            val movementId = movementsRepository.setMovement(movement)
            movement.weight?.let {
                val weight = if (weightUnit == "lb") {
                    it.poundsToKilos().toFloat()
                } else {
                    it
                }

                oneRepMaxRepository.addOneRM(
                    OneRMInfo(
                        weight = weight,
                        offsetDateTime = OffsetDateTime.now(),
                        movementId = movementId
                    )
                )
            }
        }
    }

    fun editMovement(movement: Movement) {
        viewModelScope.launch {
            movementsRepository.setMovement(movement)
        }
    }

    fun deleteMovement(id: Long) {
        viewModelScope.launch {
            movementsRepository.deleteMovement(id)
        }
    }
}

sealed interface MovementsListUiState {
    data object Loading : MovementsListUiState

    data class Success(
        val movements: List<Movement> = emptyList(),
    ) : MovementsListUiState
}

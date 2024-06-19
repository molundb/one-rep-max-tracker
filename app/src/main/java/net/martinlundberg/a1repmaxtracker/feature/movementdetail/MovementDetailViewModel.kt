package net.martinlundberg.a1repmaxtracker.feature.movementdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.repository.MovementsRepository
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MovementDetailViewModel(
    private val movementsRepository: MovementsRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementDetailUiState> = _uiState.asStateFlow()

    fun getMovementInfo(id: Int) {
        viewModelScope.launch {
            val movementDetail = Success(movementsRepository.getMovementDetail(id))
            _uiState.update { movementDetail }
        }
    }

    fun add1RM(weight: Int, movementId: Int) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            val dateTime = LocalDateTime.now().format(formatter)

            movementsRepository.addOneRM(OneRMInfo(weight, dateTime), movementId)
            getMovementInfo(movementId)

//            val currentState = _uiState.value
//            if (currentState is Success) {
//                val updated1RMs = listOf(OneRMInfo(weight, dateTime)) + currentState.movement.oneRMs
//                _uiState.value = Success(currentState.movement.copy(oneRMs = updated1RMs))
//            }
        }
    }
}

sealed interface MovementDetailUiState {
    data object Loading : MovementDetailUiState

    data class Success(
        val movement: MovementDetail,
    ) : MovementDetailUiState
}

class MovementDetailViewModelFactory(private val repository: MovementsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovementDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovementDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package net.martinlundberg.a1repmaxtracker.features.movementdetail

import androidx.lifecycle.SavedStateHandle
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

class MovementDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
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
        return Success(MovementDetail(name = "Test Movement"))
    }
}

sealed interface MovementDetailUiState {
    data object Loading : MovementDetailUiState

    data class Success(
        val movement: MovementDetail,
    ) : MovementDetailUiState
}

data class MovementDetail(
    val name: String,
    val oneRMs: List<OneRMInfo> = emptyList(),
)

data class OneRMInfo(
    val weight: Int,
    val date: String,
)
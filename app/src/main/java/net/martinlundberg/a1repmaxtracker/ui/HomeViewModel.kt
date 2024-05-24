package net.martinlundberg.a1repmaxtracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getMovements() {
        viewModelScope.launch {
            val backendResult = performBackendCall()
            _uiState.update { backendResult }
        }
    }

    // Suspend function to perform the backend call
    private suspend fun performBackendCall(): HomeUiState.Success {
        // Simulate network delay
        delay(2000)
        return HomeUiState.Success(
            listOf(
                Movement("Movement 1", 100),
                Movement("Movement 2", 6),
                Movement("Movement 3", 82),
                Movement("Movement 4", 33),
            )
        )
    }

}

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val movements: List<Movement> = emptyList(),
    ) : HomeUiState
}

data class Movement(
    val name: String,
    val weight: Int,
)
package net.martinlundberg.a1repmaxtracker.feature.movementdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.repository.OneRepMaxRepository
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MovementDetailViewModel @Inject constructor(
    private val oneRepMaxRepository: OneRepMaxRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovementDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MovementDetailUiState> = _uiState.asStateFlow()

    fun getMovementInfo(id: Long) {
        viewModelScope.launch {
            oneRepMaxRepository.getMovementDetail(id).collect { movementDetail ->
                _uiState.update { Success(movementDetail) }
            }
        }
    }

    fun add1RM(weight: Int, movementId: Long) {
        viewModelScope.launch {
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

sealed interface MovementDetailUiState {
    data object Loading : MovementDetailUiState

    data class Success(
        val movement: MovementDetail,
    ) : MovementDetailUiState
}
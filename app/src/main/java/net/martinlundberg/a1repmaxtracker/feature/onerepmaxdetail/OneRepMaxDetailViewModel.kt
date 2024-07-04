package net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.NavigationService
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.repository.OneRepMaxRepository
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Success
import javax.inject.Inject

@HiltViewModel
class OneRepMaxDetailViewModel @Inject constructor(
    private val oneRepMaxRepository: OneRepMaxRepository,
    private val navigationService: NavigationService,
) : ViewModel() {
    private val _uiState: MutableStateFlow<OneRepMaxDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<OneRepMaxDetailUiState> = _uiState.asStateFlow()

    fun getOneRepMaxDetail(id: Long) {
        viewModelScope.launch {
            val oneRmInfo = Success(oneRepMaxRepository.getOneRM(id))
            _uiState.update { oneRmInfo }
        }
    }

    fun updateOneRepMaxDetail(oneRMInfo: OneRMInfo) {
        viewModelScope.launch {
            oneRepMaxRepository.addOneRM(oneRMInfo)
        }
    }

    fun deleteOneRM(id: Long) {
        viewModelScope.launch {
            oneRepMaxRepository.deleteOneRM(id)
            navigationService.navController.popBackStack()
        }
    }
}

sealed interface OneRepMaxDetailUiState {
    data object Loading : OneRepMaxDetailUiState

    data class Success(
        val oneRMInfo: OneRMInfo,
    ) : OneRepMaxDetailUiState
}
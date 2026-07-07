package com.lifeosai.app.ui.feature.nexus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeosai.app.domain.model.NexusData
import com.lifeosai.app.domain.repository.LifeOSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NexusViewModel @Inject constructor(
    private val repository: LifeOSRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NexusUiState>(NexusUiState.Loading)
    val uiState: StateFlow<NexusUiState> = _uiState.asStateFlow()

    init {
        loadNexusData()
    }

    private fun loadNexusData() {
        viewModelScope.launch {
            repository.getNexusData()
                .onStart { _uiState.value = NexusUiState.Loading }
                .catch { e -> _uiState.value = NexusUiState.Error(e.message ?: "Unknown Error") }
                .collect { data -> _uiState.value = NexusUiState.Success(data) }
        }
    }

    fun toggleTask(taskId: String) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId)
        }
    }
}

sealed interface NexusUiState {
    data object Loading : NexusUiState
    data class Success(val data: NexusData) : NexusUiState
    data class Error(val message: String) : NexusUiState
}

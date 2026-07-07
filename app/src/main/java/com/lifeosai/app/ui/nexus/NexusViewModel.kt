package com.lifeosai.app.ui.nexus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeosai.app.domain.usecase.GetNexusDataUseCase
import com.lifeosai.app.domain.usecase.ToggleTaskCompletionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NexusViewModel @Inject constructor(
    private val getNexusDataUseCase: GetNexusDataUseCase,
    private val toggleTaskCompletionUseCase: ToggleTaskCompletionUseCase
) : ViewModel() {

    val uiState: StateFlow<NexusUiState> = getNexusDataUseCase()
        .map { data -> NexusUiState.Success(data) as NexusUiState }
        .catch { e -> emit(NexusUiState.Error(e.message ?: "Unknown Error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NexusUiState.Loading
        )

    fun toggleTask(taskId: String) {
        viewModelScope.launch {
            toggleTaskCompletionUseCase(taskId)
        }
    }
}

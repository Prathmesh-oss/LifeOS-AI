package com.lifeosai.app.ui.feature.capture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeosai.app.domain.repository.CaptureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CaptureViewModel @Inject constructor(
    private val repository: CaptureRepository
) : ViewModel() {

    private val _currentContent = MutableStateFlow("")
    private val _isCapturing = MutableStateFlow(false)

    val uiState: StateFlow<CaptureUiState> = combine(
        repository.observeRecentCaptures(),
        repository.observeFavoriteCaptures(),
        repository.observeSuggestions(),
        _currentContent,
        _isCapturing
    ) { recent, favorites, suggestions, content, capturing ->
        CaptureUiState.Success(
            recentCaptures = recent,
            favoriteCaptures = favorites,
            suggestions = suggestions,
            currentCaptureContent = content,
            isCapturing = capturing
        ) as CaptureUiState
    }.catch { e ->
        emit(CaptureUiState.Error(e.message ?: "Failed to load capture state"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CaptureUiState.Loading
    )

    fun onEvent(event: CaptureUiEvent) {
        when (event) {
            is CaptureUiEvent.OnContentChange -> _currentContent.value = event.content
            CaptureUiEvent.OnCaptureSubmit -> submitCapture()
            CaptureUiEvent.OnVoiceCaptureClick -> { /* Start Voice Recording */ }
            CaptureUiEvent.OnPhotoCaptureClick -> { /* Open Camera */ }
            CaptureUiEvent.OnCameraScanClick -> { /* Open Document Scanner */ }
            CaptureUiEvent.OnDocumentImportClick -> { /* Open File Picker */ }
            is CaptureUiEvent.OnFavoriteToggle -> toggleFavorite(event.memoryId)
            is CaptureUiEvent.OnSuggestionClick -> { /* Handle Suggestion */ }
            CaptureUiEvent.OnQuickTaskClick -> { /* AI Quick Task */ }
            CaptureUiEvent.OnQuickReminderClick -> { /* AI Quick Reminder */ }
            CaptureUiEvent.OnQuickEventClick -> { /* AI Quick Event */ }
        }
    }

    private fun submitCapture() {
        val content = _currentContent.value.trim()
        if (content.isEmpty()) return

        viewModelScope.launch {
            _isCapturing.value = true
            repository.capture(content)
            _currentContent.value = ""
            _isCapturing.value = false
        }
    }

    private fun toggleFavorite(id: String) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }
}

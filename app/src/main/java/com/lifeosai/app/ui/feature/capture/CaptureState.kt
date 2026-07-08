package com.lifeosai.app.ui.feature.capture

import androidx.compose.runtime.Immutable
import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.repository.CaptureSuggestion

@Immutable
sealed interface CaptureUiState {
    data object Loading : CaptureUiState
    
    data class Success(
        val recentCaptures: List<Memory> = emptyList(),
        val favoriteCaptures: List<Memory> = emptyList(),
        val suggestions: List<CaptureSuggestion> = emptyList(),
        val currentCaptureContent: String = "",
        val isCapturing: Boolean = false
    ) : CaptureUiState

    data class Error(val message: String) : CaptureUiState
}

sealed interface CaptureUiEvent {
    data class OnContentChange(val content: String) : CaptureUiEvent
    data object OnCaptureSubmit : CaptureUiEvent
    data object OnVoiceCaptureClick : CaptureUiEvent
    data object OnPhotoCaptureClick : CaptureUiEvent
    data object OnCameraScanClick : CaptureUiEvent
    data object OnDocumentImportClick : CaptureUiEvent
    data class OnFavoriteToggle(val memoryId: String) : CaptureUiEvent
    data class OnSuggestionClick(val suggestionId: String) : CaptureUiEvent
    data object OnQuickTaskClick : CaptureUiEvent
    data object OnQuickReminderClick : CaptureUiEvent
    data object OnQuickEventClick : CaptureUiEvent
}

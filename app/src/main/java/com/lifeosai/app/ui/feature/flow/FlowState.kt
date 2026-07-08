package com.lifeosai.app.ui.feature.flow

import androidx.compose.runtime.Immutable
import com.lifeosai.app.domain.repository.FocusRecommendation
import com.lifeosai.app.domain.repository.FlowStats
import com.lifeosai.app.domain.repository.FocusSession

@Immutable
sealed interface FlowUiState {
    data object Loading : FlowUiState
    
    data class Success(
        val greeting: String,
        val currentSession: FocusSession? = null,
        val stats: FlowStats = FlowStats(),
        val upcomingSessions: List<FocusSession> = emptyList(),
        val recommendations: List<FocusRecommendation> = emptyList(),
        val history: List<FocusSession> = emptyList(),
        val isRefreshing: Boolean = false,
        val isPhoneSilent: Boolean = true
    ) : FlowUiState

    data class Error(val message: String) : FlowUiState
}

sealed interface FlowUiEvent {
    data object OnRefresh : FlowUiEvent
    data class OnStartSession(val name: String, val duration: Int) : FlowUiEvent
    data object OnPauseSession : FlowUiEvent
    data object OnResumeSession : FlowUiEvent
    data object OnStopSession : FlowUiEvent
    data class OnRecommendationClick(val id: String) : FlowUiEvent
    data object OnToggleSilence : FlowUiEvent
    data object OnVoiceAssistantClick : FlowUiEvent
}

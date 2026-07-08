package com.lifeosai.app.ui.feature.brain

import androidx.compose.runtime.Immutable
import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.repository.AIInsight
import com.lifeosai.app.domain.repository.BrainStats

@Immutable
sealed interface BrainUiState {
    data object Loading : BrainUiState
    
    data class Success(
        val greeting: String,
        val stats: BrainStats,
        val insights: List<AIInsight>,
        val groupedMemories: Map<String, List<Memory>>,
        val searchResults: List<Memory> = emptyList(),
        val searchQuery: String = "",
        val isSearching: Boolean = false,
        val isRefreshing: Boolean = false
    ) : BrainUiState

    data class Error(val message: String) : BrainUiState
}

/**
 * UI Events for the Brain screen.
 */
sealed interface BrainUiEvent {
    data class OnSearchQueryChange(val query: String) : BrainUiEvent
    data object OnRefresh : BrainUiEvent
    data object OnVoiceAssistantClick : BrainUiEvent
    data class OnMemoryClick(val memoryId: String) : BrainUiEvent
    data class OnInsightClick(val insightId: String) : BrainUiEvent
}

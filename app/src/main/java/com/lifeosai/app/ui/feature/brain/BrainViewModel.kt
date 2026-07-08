package com.lifeosai.app.ui.feature.brain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.repository.BrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BrainViewModel @Inject constructor(
    private val repository: BrainRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<BrainUiState> = combine(
        repository.observeMemories(),
        repository.observeStats(),
        repository.observeInsights(),
        _searchQuery,
        _isRefreshing
    ) { memories, stats, insights, query, refreshing ->
        if (query.isEmpty()) {
            BrainUiState.Success(
                greeting = repository.getDynamicGreeting(),
                stats = stats,
                insights = insights,
                groupedMemories = groupMemories(memories),
                searchQuery = query,
                isSearching = false,
                isRefreshing = refreshing
            ) as BrainUiState
        } else {
            val searchResults = memories.filter { 
                it.content.contains(query, ignoreCase = true) || 
                it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
            BrainUiState.Success(
                greeting = repository.getDynamicGreeting(),
                stats = stats,
                insights = insights,
                groupedMemories = emptyMap(),
                searchResults = searchResults,
                searchQuery = query,
                isSearching = true,
                isRefreshing = refreshing
            ) as BrainUiState
        }
    }.catch { e ->
        emit(BrainUiState.Error(e.message ?: "An unexpected error occurred"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BrainUiState.Loading
    )

    fun onEvent(event: BrainUiEvent) {
        when (event) {
            is BrainUiEvent.OnSearchQueryChange -> _searchQuery.value = event.query
            BrainUiEvent.OnRefresh -> refresh()
            BrainUiEvent.OnVoiceAssistantClick -> { /* Handle voice assistant */ }
            is BrainUiEvent.OnMemoryClick -> { /* Navigate to memory detail */ }
            is BrainUiEvent.OnInsightClick -> { /* Handle insight action */ }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refresh()
            _isRefreshing.value = false
        }
    }

    private fun groupMemories(memories: List<Memory>): Map<String, List<Memory>> {
        val now = LocalDateTime.now()
        return memories.groupBy { memory ->
            when {
                memory.createdAt.toLocalDate() == now.toLocalDate() -> "Today"
                memory.createdAt.toLocalDate() == now.toLocalDate().minusDays(1) -> "Yesterday"
                memory.createdAt.isAfter(now.minusWeeks(1)) -> "Last Week"
                else -> memory.createdAt.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
            }
        }
    }
}

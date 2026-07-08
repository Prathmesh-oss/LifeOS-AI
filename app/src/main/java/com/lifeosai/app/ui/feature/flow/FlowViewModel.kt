package com.lifeosai.app.ui.feature.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeosai.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class FlowViewModel @Inject constructor(
    private val repository: FlowRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<FlowUiState> = combine(
        listOf(
            repository.observeCurrentSession(),
            repository.observeTodayStats(),
            repository.observeUpcomingSessions(),
            repository.observeRecommendations(),
            repository.observeHistory(),
            _isRefreshing
        )
    ) { args: Array<Any?> ->
        val current = args[0] as FocusSession?
        val stats = args[1] as FlowStats
        val upcoming = args[2] as List<FocusSession>
        val recommendations = args[3] as List<FocusRecommendation>
        val history = args[4] as List<FocusSession>
        val refreshing = args[5] as Boolean

        FlowUiState.Success(
            greeting = getDynamicGreeting(),
            currentSession = current,
            stats = stats,
            upcomingSessions = upcoming,
            recommendations = recommendations,
            history = history,
            isRefreshing = refreshing
        ) as FlowUiState
    }.catch { e ->
        emit(FlowUiState.Error(e.message ?: "An unexpected error occurred"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FlowUiState.Loading
    )

    fun onEvent(event: FlowUiEvent) {
        when (event) {
            FlowUiEvent.OnRefresh -> refresh()
            is FlowUiEvent.OnStartSession -> {
                viewModelScope.launch {
                    repository.startSession(event.name, event.duration)
                }
            }
            FlowUiEvent.OnPauseSession -> {
                viewModelScope.launch { repository.pauseSession() }
            }
            FlowUiEvent.OnResumeSession -> {
                viewModelScope.launch { repository.resumeSession() }
            }
            FlowUiEvent.OnStopSession -> {
                viewModelScope.launch { repository.stopSession() }
            }
            is FlowUiEvent.OnRecommendationClick -> {
                // Handle recommendation action
            }
            FlowUiEvent.OnToggleSilence -> {
                // Handle silence toggle
            }
            FlowUiEvent.OnVoiceAssistantClick -> {
                // Handle voice assistant
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refresh()
            _isRefreshing.value = false
        }
    }

    private fun getDynamicGreeting(): String {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 5..11 -> "Good Morning, focus is high now."
            in 12..16 -> "Good Afternoon, stay productive."
            in 17..21 -> "Your peak focus window has started."
            else -> "Ready for a deep work session?"
        }
    }
}

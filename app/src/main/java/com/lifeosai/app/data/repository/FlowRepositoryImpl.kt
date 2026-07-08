package com.lifeosai.app.data.repository

import com.lifeosai.app.domain.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlowRepositoryImpl @Inject constructor() : FlowRepository {

    private val _currentSession = MutableStateFlow<FocusSession?>(null)
    private val _history = MutableStateFlow<List<FocusSession>>(emptyList())

    override fun observeCurrentSession(): Flow<FocusSession?> = _currentSession.asStateFlow()

    override fun observeTodayStats(): Flow<FlowStats> = flow {
        // Simulated stats calculation
        emit(
            FlowStats(
                totalFocusedHours = 4.5f,
                sessionsCompleted = 3,
                totalDistractions = 2,
                averageFocusScore = 88,
                dailyStreak = 5
            )
        )
    }

    override fun observeUpcomingSessions(): Flow<List<FocusSession>> = flowOf(
        listOf(
            FocusSession(
                id = "next_1",
                name = "App Design Review",
                startTime = LocalDateTime.now().plusHours(1),
                durationMinutes = 60,
                remainingSeconds = 3600,
                status = SessionStatus.ACTIVE
            ),
            FocusSession(
                id = "next_2",
                name = "Clean Architecture Refactor",
                startTime = LocalDateTime.now().plusHours(3),
                durationMinutes = 90,
                remainingSeconds = 5400,
                status = SessionStatus.ACTIVE
            )
        )
    )

    override fun observeHistory(): Flow<List<FocusSession>> = _history.asStateFlow()

    override fun observeRecommendations(): Flow<List<FocusRecommendation>> = flowOf(
        listOf(
            FocusRecommendation("1", "Take a 5-minute break.", RecommendationType.BREAK),
            FocusRecommendation("2", "You focus best after hydration.", RecommendationType.HYDRATION),
            FocusRecommendation("3", "Your peak focus window has started.", RecommendationType.ENVIRONMENT)
        )
    )

    override suspend fun startSession(name: String, durationMinutes: Int) {
        val session = FocusSession(
            id = UUID.randomUUID().toString(),
            name = name,
            startTime = LocalDateTime.now(),
            durationMinutes = durationMinutes,
            remainingSeconds = durationMinutes * 60L,
            status = SessionStatus.ACTIVE
        )
        _currentSession.value = session
    }

    override suspend fun pauseSession() {
        _currentSession.value = _currentSession.value?.copy(status = SessionStatus.PAUSED)
    }

    override suspend fun resumeSession() {
        _currentSession.value = _currentSession.value?.copy(status = SessionStatus.ACTIVE)
    }

    override suspend fun stopSession() {
        _currentSession.value?.let { session ->
            val completedSession = session.copy(status = SessionStatus.COMPLETED)
            _history.value = listOf(completedSession) + _history.value
        }
        _currentSession.value = null
    }

    override suspend fun refresh() {
        delay(1000) // Simulate network refresh
    }
}

package com.lifeosai.app.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Repository interface for the Flow (Deep Work) system.
 * Resides in the domain layer as per Clean Architecture.
 */
interface FlowRepository {
    /**
     * Observes the current active focus session, if any.
     */
    fun observeCurrentSession(): Flow<FocusSession?>

    /**
     * Observes today's focus statistics.
     */
    fun observeTodayStats(): Flow<FlowStats>

    /**
     * Observes upcoming scheduled focus sessions.
     */
    fun observeUpcomingSessions(): Flow<List<FocusSession>>

    /**
     * Observes recent session history.
     */
    fun observeHistory(): Flow<List<FocusSession>>

    /**
     * Observes AI-driven recommendations for focus.
     */
    fun observeRecommendations(): Flow<List<FocusRecommendation>>

    /**
     * Starts a new focus session.
     */
    suspend fun startSession(name: String, durationMinutes: Int)

    /**
     * Pauses the current session.
     */
    suspend fun pauseSession()

    /**
     * Resumes the current session.
     */
    suspend fun resumeSession()

    /**
     * Stops and saves the current session.
     */
    suspend fun stopSession()

    /**
     * Refreshes flow data.
     */
    suspend fun refresh()
}

/**
 * Domain model for a Focus Session.
 */
data class FocusSession(
    val id: String,
    val name: String,
    val startTime: LocalDateTime,
    val durationMinutes: Int,
    val remainingSeconds: Long,
    val status: SessionStatus,
    val focusScore: Int? = null,
    val distractions: Int = 0
)

enum class SessionStatus {
    ACTIVE, PAUSED, COMPLETED, CANCELLED
}

/**
 * Statistics for focus performance.
 */
data class FlowStats(
    val totalFocusedHours: Float = 0f,
    val sessionsCompleted: Int = 0,
    val totalDistractions: Int = 0,
    val averageFocusScore: Int = 0,
    val dailyStreak: Int = 0
)

/**
 * AI Recommendation for focus.
 */
data class FocusRecommendation(
    val id: String,
    val message: String,
    val type: RecommendationType
)

enum class RecommendationType {
    BREAK, HYDRATION, ENVIRONMENT, PLANNING
}

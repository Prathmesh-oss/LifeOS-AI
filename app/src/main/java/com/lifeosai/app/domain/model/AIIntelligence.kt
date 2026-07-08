package com.lifeosai.app.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents the classified intention of a user interaction.
 * The Intent Classifier uses this to route requests.
 */
sealed class AIIntent {
    data class CreateTask(val title: String, val deadline: LocalDateTime? = null, val priority: Priority = Priority.MEDIUM) : AIIntent()
    data class ScheduleEvent(val title: String, val startTime: LocalDateTime, val durationMinutes: Int = 30) : AIIntent()
    data class CaptureMemory(val content: String, val type: MemoryType) : AIIntent()
    data class SeekInsight(val query: String) : AIIntent()
    data class PlanRequest(val timeframe: Timeframe) : AIIntent()
    data object AnalyzeState : AIIntent()
    data object Unknown : AIIntent()
}

/**
 * The atomic unit of user history and knowledge.
 * Stored in Room for semantic retrieval and offline reasoning.
 */
data class AIMemory(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val type: MemoryType,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val importance: Float = 0.5f,
    val metadata: Map<String, String> = emptyMap(),
    val embedding: List<Float>? = null // For future Vector Search/Semantic retrieval
)

enum class MemoryType {
    CONVERSATION, TASK_HISTORY, USER_PREFERENCE, INSIGHT, MOMENT, GOAL
}

enum class Priority { LOW, MEDIUM, HIGH, CRITICAL }

enum class Timeframe { TODAY, TOMORROW, WEEK, MONTH }

/**
 * A comprehensive snapshot of the user's current state.
 * This is the "Fuel" for the Reasoning Engine.
 */
data class UserContext(
    val currentTime: LocalDateTime = LocalDateTime.now(),
    val activeScreen: String? = null,
    val energyLevel: Int? = null, // 1-10 scale
    val focusMode: Boolean = false,
    val recentMemories: List<AIMemory> = emptyList(),
    val batteryLevel: Int = 100,
    val location: String? = null
)

/**
 * The structured output of the Intelligence Layer.
 */
sealed class AIResponse {
    data class Text(val message: String) : AIResponse()
    data class ActionRequired(val message: String, val intent: AIIntent) : AIResponse()
    data class Recommendation(val title: String, val description: String, val confidence: Float) : AIResponse()
    data class Error(val message: String, val throwable: Throwable? = null) : AIResponse()
}

/**
 * Core interface for the AI Orchestrator.
 * Orchestrator -> Intent Classifier -> Reasoning Engine -> Memory Engine -> Plan Engine.
 */
interface IntelligenceOrchestrator {
    suspend fun process(input: String, context: UserContext): AIResponse
    suspend fun synthesizeDailyBriefing(context: UserContext): AIResponse
    suspend fun predictNextAction(context: UserContext): AIIntent
}

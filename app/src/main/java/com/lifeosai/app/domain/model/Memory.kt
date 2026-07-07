package com.lifeosai.app.domain.model

import java.time.LocalDateTime

/**
 * The core domain model for a "Memory" in LifeOS AI.
 * A memory is any piece of information the AI "remembers" about the user.
 */
data class Memory(
    val id: String,
    val content: String,
    val type: MemoryType,
    val importance: Float, // 0.0 to 1.0
    val createdAt: LocalDateTime,
    val lastAccessedAt: LocalDateTime,
    val metadata: Map<String, String> = emptyMap(),
    val tags: List<String> = emptyList(),
    val summary: String? = null
)

enum class MemoryType {
    CONVERSATION,
    IDEA,
    NOTE,
    VOICE_TRANSCRIPT,
    AI_INSIGHT,
    TASK_HISTORY,
    PROJECT_HISTORY,
    SYSTEM_LOG
}

/**
 * Represents the context built from memories to be fed into an LLM.
 */
data class MemoryContext(
    val relevantMemories: List<Memory>,
    val summary: String,
    val totalTokenCount: Int
)

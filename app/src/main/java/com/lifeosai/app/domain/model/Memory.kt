package com.lifeosai.app.domain.model

import java.time.LocalDateTime

/**
 * The core domain model for a "Memory" in LifeOS AI.
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

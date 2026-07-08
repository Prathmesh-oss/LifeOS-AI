package com.lifeosai.app.domain.model

import java.time.LocalDateTime

/**
 * The core domain model for a "Memory" in LifeOS AI.
 * Represents any piece of captured information, thought, or interaction.
 */
data class Memory(
    val id: String,
    val content: String,
    val type: MemoryType,
    val importance: Float = 0.5f, // 0.0 to 1.0
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastAccessedAt: LocalDateTime = LocalDateTime.now(),
    val metadata: Map<String, String> = emptyMap(),
    val tags: List<String> = emptyList(),
    val summary: String? = null,
    val isFavorite: Boolean = false
)

/**
 * Categorization of captured content.
 * These types drive the AI classification and UI presentation.
 */
enum class MemoryType {
    IDEA,
    TASK,
    MEMORY,
    PROJECT,
    MEETING,
    QUOTE,
    BOOKMARK,
    VOICE,
    CONVERSATION,
    AI_INSIGHT,
    SYSTEM_LOG
}

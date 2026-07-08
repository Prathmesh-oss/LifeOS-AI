package com.lifeosai.app.domain.repository

import com.lifeosai.app.domain.model.Memory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for aggregated data on the Brain screen.
 * Follows Clean Architecture principles by residing in the domain layer.
 */
interface BrainRepository {
    /**
     * Observes all memories, typically sorted by creation date.
     */
    fun observeMemories(): Flow<List<Memory>>

    /**
     * Observes statistical data about the user's second brain.
     */
    fun observeStats(): Flow<BrainStats>

    /**
     * Observes AI-generated insights based on user activity and memories.
     */
    fun observeInsights(): Flow<List<AIInsight>>

    /**
     * Returns a dynamic greeting based on time of day and user name.
     */
    fun getDynamicGreeting(): String

    /**
     * Performs a deep search across all brain contents.
     */
    suspend fun search(query: String): List<Memory>

    /**
     * Refreshes brain data from remote sources.
     */
    suspend fun refresh()
}

/**
 * Data model for Brain screen statistics.
 */
data class BrainStats(
    val totalMemories: Int = 0,
    val aiConversations: Int = 0,
    val voiceNotes: Int = 0,
    val documents: Int = 0
)

/**
 * Data model for an AI-generated insight.
 */
data class AIInsight(
    val id: String,
    val text: String,
    val emoji: String
)

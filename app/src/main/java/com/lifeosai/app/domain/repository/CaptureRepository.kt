package com.lifeosai.app.domain.repository

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for the Capture system.
 * Handles instant thought capture, AI categorization, and suggestions.
 */
interface CaptureRepository {
    /**
     * Captures a new thought/item and automatically categorizes it using AI.
     */
    suspend fun capture(content: String): Memory

    /**
     * Observes recent captures for the timeline.
     */
    fun observeRecentCaptures(): Flow<List<Memory>>

    /**
     * Observes favorite captures.
     */
    fun observeFavoriteCaptures(): Flow<List<Memory>>

    /**
     * Gets AI-driven suggestions based on the current context or content being captured.
     */
    fun observeSuggestions(): Flow<List<CaptureSuggestion>>

    /**
     * Manually saves or updates a memory.
     */
    suspend fun saveCapture(memory: Memory)

    /**
     * Toggles the favorite status of a capture.
     */
    suspend fun toggleFavorite(memoryId: String)
}

/**
 * Model for AI-generated capture suggestions.
 */
data class CaptureSuggestion(
    val id: String,
    val text: String,
    val type: SuggestionType
)

enum class SuggestionType {
    CLASSIFICATION,
    ACTION,
    ENRICHMENT
}

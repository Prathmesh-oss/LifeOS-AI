package com.lifeosai.app.ai.memory

import com.lifeosai.app.domain.model.Memory
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp

/**
 * Scoring engine for memories.
 * Implements a time-decay model: Score = Importance * exp(-DecayConstant * TimeElapsed)
 */
@Singleton
class MemoryScorer @Inject constructor() {

    private val decayConstant = 0.005 // Adjust for faster/slower decay

    /**
     * Calculates the retrieval score for a memory.
     * Higher score = more likely to be used in context.
     */
    fun calculateScore(memory: Memory, currentQuery: String? = null): Float {
        val now = LocalDateTime.now()
        val hoursElapsed = Duration.between(memory.lastAccessedAt, now).toHours().toDouble()
        
        // Base Recency Score
        val recencyScore = exp(-decayConstant * hoursElapsed).toFloat()
        
        // Semantic Relevance (Stub for actual vector similarity)
        val relevanceScore = if (currentQuery != null && memory.content.contains(currentQuery, ignoreCase = true)) {
            1.0f
        } else {
            0.5f
        }

        return (memory.importance * 0.4f) + (recencyScore * 0.3f) + (relevanceScore * 0.3f)
    }

    /**
     * Ranks a list of memories and returns the top N.
     */
    fun rank(memories: List<Memory>, query: String? = null, limit: Int = 5): List<Memory> {
        return memories.sortedByDescending { calculateScore(it, query) }.take(limit)
    }
}

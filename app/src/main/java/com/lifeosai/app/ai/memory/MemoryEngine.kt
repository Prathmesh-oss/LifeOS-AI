package com.lifeosai.app.ai.memory

import kotlinx.coroutines.flow.Flow

interface MemoryEngine {
    /**
     * Stores a piece of information with semantic metadata.
     */
    suspend fun record(content: String, metadata: Map<String, String>)

    /**
     * Retrieves semantically relevant memories based on a query.
     */
    suspend fun search(query: String, limit: Int = 5): List<MemoryEntry>

    /**
     * Provides a stream of relevant memories as context changes.
     */
    fun observeRelevantMemories(contextQuery: String): Flow<List<MemoryEntry>>
}

data class MemoryEntry(
    val id: String,
    val content: String,
    val timestamp: Long,
    val importance: Float,
    val vectorEmbedding: FloatArray? = null
)

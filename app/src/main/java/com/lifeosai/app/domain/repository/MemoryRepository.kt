package com.lifeosai.app.domain.repository

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing LifeOS memories.
 * Supports reactive streams and complex retrieval logic.
 */
interface MemoryRepository {
    
    /**
     * Streams all memories, usually for the timeline view.
     */
    fun observeAllMemories(): Flow<List<Memory>>

    /**
     * Retrieves memories by type.
     */
    fun observeMemoriesByType(type: MemoryType): Flow<List<Memory>>

    /**
     * Saves or updates a memory.
     */
    suspend fun saveMemory(memory: Memory)

    /**
     * Deletes a memory by ID.
     */
    suspend fun deleteMemory(id: String)

    /**
     * Performs a semantic search across memories.
     * In production, this would leverage vector embeddings.
     */
    suspend fun searchMemories(query: String, limit: Int = 10): List<Memory>

    /**
     * Marks a memory as accessed to update its "Recency" score.
     */
    suspend fun markAsAccessed(id: String)
}

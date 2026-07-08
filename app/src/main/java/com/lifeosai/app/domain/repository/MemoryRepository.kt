package com.lifeosai.app.domain.repository

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing LifeOS memories.
 */
interface MemoryRepository {
    fun observeAllMemories(): Flow<List<Memory>>
    fun observeMemoriesByType(type: MemoryType): Flow<List<Memory>>
    suspend fun saveMemory(memory: Memory)
    suspend fun deleteMemory(id: String)
    suspend fun searchMemories(query: String, limit: Int = 10): List<Memory>
}

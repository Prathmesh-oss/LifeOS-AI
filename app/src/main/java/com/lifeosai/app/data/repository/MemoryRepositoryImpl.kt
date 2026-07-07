package com.lifeosai.app.data.repository

import com.lifeosai.app.data.dao.MemoryDao
import com.lifeosai.app.data.entity.MemoryEntity
import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import com.lifeosai.app.domain.repository.MemoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepositoryImpl @Inject constructor(
    private val memoryDao: MemoryDao
) : MemoryRepository {

    override fun observeAllMemories(): Flow<List<Memory>> {
        return memoryDao.getAllMemories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeMemoriesByType(type: MemoryType): Flow<List<Memory>> {
        return memoryDao.getAllMemories().map { entities ->
            entities.map { it.toDomain() }.filter { it.type == type }
        }
    }

    override suspend fun saveMemory(memory: Memory) {
        memoryDao.insertMemory(memory.toEntity())
    }

    override suspend fun deleteMemory(id: String) {
        memoryDao.deleteMemory(id)
    }

    override suspend fun searchMemories(query: String, limit: Int): List<Memory> {
        // Semantic search would go here (e.g. vector search)
        return emptyList()
    }

    override suspend fun markAsAccessed(id: String) {
        memoryDao.updateLastAccessed(id)
    }

    private fun MemoryEntity.toDomain(): Memory {
        return Memory(
            id = id,
            content = content,
            type = MemoryType.NOTE, // Simplified mapping
            importance = importance / 100f,
            createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
            lastAccessedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastAccessedAt), ZoneId.systemDefault())
        )
    }

    private fun Memory.toEntity(): MemoryEntity {
        return MemoryEntity(
            id = id,
            content = content,
            importance = (importance * 100).toInt(),
            createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            lastAccessedAt = lastAccessedAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
}

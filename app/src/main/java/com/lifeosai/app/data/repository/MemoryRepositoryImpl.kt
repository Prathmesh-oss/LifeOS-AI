package com.lifeosai.app.data.repository

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import com.lifeosai.app.domain.repository.MemoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepositoryImpl @Inject constructor() : MemoryRepository {

    private val _memories = MutableStateFlow(
        listOf(
            Memory(
                id = UUID.randomUUID().toString(),
                content = "LifeOS AI is the world's first AI Life Operating System.",
                type = MemoryType.AI_INSIGHT,
                importance = 0.9f,
                createdAt = LocalDateTime.now().minusHours(2),
                lastAccessedAt = LocalDateTime.now(),
                tags = listOf("philosophy", "product")
            ),
            Memory(
                id = UUID.randomUUID().toString(),
                content = "Remember to buy premium dark roast coffee for the office.",
                type = MemoryType.NOTE,
                importance = 0.5f,
                createdAt = LocalDateTime.now().minusDays(1),
                lastAccessedAt = LocalDateTime.now().minusDays(1),
                tags = listOf("shopping")
            ),
            Memory(
                id = UUID.randomUUID().toString(),
                content = "Meeting with the design team about the new AMOLED dashboard.",
                type = MemoryType.CONVERSATION,
                importance = 0.8f,
                createdAt = LocalDateTime.now().minusDays(2),
                lastAccessedAt = LocalDateTime.now().minusDays(2),
                tags = listOf("design", "amoled")
            )
        )
    )

    override fun observeAllMemories(): Flow<List<Memory>> = _memories.asStateFlow()

    override fun observeMemoriesByType(type: MemoryType): Flow<List<Memory>> = 
        _memories.map { memories -> memories.filter { it.type == type } }

    override suspend fun saveMemory(memory: Memory) {
        _memories.value = _memories.value + memory
    }

    override suspend fun deleteMemory(id: String) {
        _memories.value = _memories.value.filter { it.id != id }
    }

    override suspend fun searchMemories(query: String, limit: Int): List<Memory> {
        return _memories.value.filter { it.content.contains(query, ignoreCase = true) }.take(limit)
    }
}

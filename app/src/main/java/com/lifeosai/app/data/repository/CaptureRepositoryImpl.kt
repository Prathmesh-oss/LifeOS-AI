package com.lifeosai.app.data.repository

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import com.lifeosai.app.domain.repository.CaptureRepository
import com.lifeosai.app.domain.repository.CaptureSuggestion
import com.lifeosai.app.domain.repository.MemoryRepository
import com.lifeosai.app.domain.repository.SuggestionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaptureRepositoryImpl @Inject constructor(
    private val memoryRepository: MemoryRepository
) : CaptureRepository {

    override suspend fun capture(content: String): Memory {
        val type = autoCategorize(content)
        val memory = Memory(
            id = UUID.randomUUID().toString(),
            content = content,
            type = type,
            createdAt = LocalDateTime.now(),
            lastAccessedAt = LocalDateTime.now()
        )
        memoryRepository.saveMemory(memory)
        return memory
    }

    override fun observeRecentCaptures(): Flow<List<Memory>> {
        return memoryRepository.observeAllMemories()
            .map { memories -> 
                memories.sortedByDescending { it.createdAt }.take(10) 
            }
    }

    override fun observeFavoriteCaptures(): Flow<List<Memory>> {
        return memoryRepository.observeAllMemories()
            .map { memories -> 
                memories.filter { it.isFavorite }.sortedByDescending { it.createdAt }
            }
    }

    override fun observeSuggestions(): Flow<List<CaptureSuggestion>> {
        // Mocked AI suggestions
        return flowOf(
            listOf(
                CaptureSuggestion("1", "This sounds like a project.", SuggestionType.CLASSIFICATION),
                CaptureSuggestion("2", "I can summarize this.", SuggestionType.ENRICHMENT),
                CaptureSuggestion("3", "Should I create a reminder for this?", SuggestionType.ACTION)
            )
        )
    }

    override suspend fun saveCapture(memory: Memory) {
        memoryRepository.saveMemory(memory)
    }

    override suspend fun toggleFavorite(memoryId: String) {
        // Note: In a real implementation, we would get the memory, toggle its status, and save it.
        // For now, this is a placeholder as MemoryRepository might need an update to support partial updates or retrieval.
    }

    private fun autoCategorize(content: String): MemoryType {
        val lowerContent = content.lowercase()
        return when {
            lowerContent.contains("buy") || lowerContent.contains("todo") || lowerContent.contains("remember to") -> MemoryType.TASK
            lowerContent.contains("project") || lowerContent.contains("build") -> MemoryType.PROJECT
            lowerContent.contains("meeting") || lowerContent.contains("call with") -> MemoryType.MEETING
            lowerContent.contains("http") || lowerContent.contains("www") -> MemoryType.BOOKMARK
            lowerContent.length < 50 -> MemoryType.IDEA
            else -> MemoryType.MEMORY
        }
    }
}

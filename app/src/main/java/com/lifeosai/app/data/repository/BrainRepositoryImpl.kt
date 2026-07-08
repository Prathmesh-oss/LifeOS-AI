package com.lifeosai.app.data.repository

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import com.lifeosai.app.domain.repository.AIInsight
import com.lifeosai.app.domain.repository.BrainRepository
import com.lifeosai.app.domain.repository.BrainStats
import com.lifeosai.app.domain.repository.MemoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrainRepositoryImpl @Inject constructor(
    private val memoryRepository: MemoryRepository
) : BrainRepository {

    override fun observeMemories(): Flow<List<Memory>> {
        return memoryRepository.observeAllMemories()
            .map { memories -> memories.sortedByDescending { it.createdAt } }
    }

    override fun observeStats(): Flow<BrainStats> {
        return memoryRepository.observeAllMemories().map { memories ->
            BrainStats(
                totalMemories = memories.size,
                aiConversations = memories.count { it.type == MemoryType.CONVERSATION },
                voiceNotes = memories.count { it.type == MemoryType.VOICE_TRANSCRIPT },
                documents = memories.count { it.type == MemoryType.NOTE }
            )
        }
    }

    override fun observeInsights(): Flow<List<AIInsight>> {
        // In a real app, this would come from an AI service or local analysis
        return memoryRepository.observeAllMemories().map { memories ->
            val insights = mutableListOf<AIInsight>()
            
            val androidMentions = memories.count { it.content.contains("Android", ignoreCase = true) }
            if (androidMentions > 5) {
                insights.add(AIInsight("1", "You've mentioned Android $androidMentions times this week.", "🤖"))
            }

            val lateNightIdeas = memories.count { 
                it.type == MemoryType.IDEA && it.createdAt.toLocalTime().isAfter(LocalTime.of(22, 0)) 
            }
            if (lateNightIdeas > 2) {
                insights.add(AIInsight("2", "You often save ideas after 10 PM. Peak creativity?", "🌙"))
            }

            insights
        }
    }

    override fun getDynamicGreeting(): String {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..21 -> "Good Evening"
            else -> "Hello"
        }
    }

    override suspend fun search(query: String): List<Memory> {
        return memoryRepository.searchMemories(query)
    }

    override suspend fun refresh() {
        // Simulate network delay for pull-to-refresh
        delay(1500)
    }
}

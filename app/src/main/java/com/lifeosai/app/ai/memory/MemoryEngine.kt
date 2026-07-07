package com.lifeosai.app.ai.memory

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryContext
import com.lifeosai.app.domain.repository.MemoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The high-level Memory Engine for LifeOS AI.
 * Orchestrates memory storage, ranking, and context building.
 */
@Singleton
class MemoryEngine @Inject constructor(
    private val repository: MemoryRepository,
    private val scorer: MemoryScorer,
    private val contextBuilder: MemoryContextBuilder
) {

    /**
     * Records a new memory into the user's second brain.
     */
    suspend fun record(memory: Memory) {
        repository.saveMemory(memory)
    }

    /**
     * Retrieves the best context for a specific query or situation.
     */
    suspend fun getContextForQuery(query: String, limit: Int = 10): MemoryContext {
        // 1. Get semantically relevant memories from DB
        val potentialMemories = repository.searchMemories(query, limit = 50)
        
        // 2. Rank them using recency/importance scorer
        val rankedMemories = scorer.rank(potentialMemories, query, limit)
        
        // 3. Build the context object for the LLM
        return contextBuilder.build(rankedMemories)
    }

    /**
     * Provides a chronological stream of memories.
     */
    fun getTimeline(): Flow<List<Memory>> {
        return repository.observeAllMemories()
    }
}

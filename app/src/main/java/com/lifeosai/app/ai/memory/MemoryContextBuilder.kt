package com.lifeosai.app.ai.memory

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryContext
import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Builds the text context for LLM prompts from a list of relevant memories.
 */
@Singleton
class MemoryContextBuilder @Inject constructor() {

    /**
     * Constructs a formatted string representing the user's "Second Brain" state.
     */
    fun build(relevantMemories: List<Memory>): MemoryContext {
        val sb = StringBuilder()
        sb.append("--- USER MEMORY CONTEXT ---\n")
        
        relevantMemories.forEach { memory ->
            sb.append("[${memory.type}] (${memory.createdAt}): ${memory.content}\n")
            if (!memory.tags.isNullOrEmpty()) {
                sb.append("Tags: ${memory.tags.joinToString(", ")}\n")
            }
            sb.append("\n")
        }
        
        sb.append("--- END CONTEXT ---")

        val contextString = sb.toString()
        
        return MemoryContext(
            relevantMemories = relevantMemories,
            summary = contextString,
            totalTokenCount = estimateTokenCount(contextString)
        )
    }

    /**
     * Simple heuristic for token counting (approx 4 chars per token).
     */
    private fun estimateTokenCount(text: String): Int {
        return text.length / 4
    }
}

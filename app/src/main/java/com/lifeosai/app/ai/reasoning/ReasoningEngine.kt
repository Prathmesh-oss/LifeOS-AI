package com.lifeosai.app.ai.reasoning

import com.lifeosai.app.ai.core.model.AIContext
import com.lifeosai.app.ai.core.model.AIResult
import kotlinx.coroutines.flow.Flow

interface ReasoningEngine {
    /**
     * The core "Thinking" process. Orchestrates between memory, DNA, and tools.
     */
    fun reason(input: String, context: AIContext): Flow<AIResult<ThoughtProcess>>
}

data class ThoughtProcess(
    val reasoningChain: List<String>,
    val finalDecision: String,
    val actions: List<AIAction>
)

data class AIAction(
    val type: String,
    val payload: Map<String, String>
)

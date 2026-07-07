package com.lifeosai.app.domain.usecase.ai

import com.lifeosai.app.ai.core.model.AIContext
import com.lifeosai.app.ai.core.model.AIResult
import com.lifeosai.app.ai.reasoning.ReasoningEngine
import com.lifeosai.app.ai.reasoning.ThoughtProcess
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * High-level Use Case to get a smart recommendation from the AI.
 * This orchestrates the ReasoningEngine.
 */
class GetSmartIntelligenceUseCase @Inject constructor(
    private val reasoningEngine: ReasoningEngine
) {
    operator fun invoke(query: String, context: AIContext): Flow<AIResult<ThoughtProcess>> {
        return reasoningEngine.reason(query, context)
    }
}

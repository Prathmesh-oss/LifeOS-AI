package com.lifeosai.app.ai.reasoning

import kotlinx.coroutines.flow.Flow

interface ReasoningEngine {
    fun infer(context: AIContext): Flow<InferenceResult>
}

data class AIContext(
    val query: String,
    val memorySnapshot: List<String>,
    val dnaSnapshot: Map<String, Any>
)

data class InferenceResult(
    val primaryThought: String,
    val suggestedActions: List<String>,
    val confidence: Float
)

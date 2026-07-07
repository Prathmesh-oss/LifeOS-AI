package com.lifeosai.app.ai

import kotlinx.coroutines.flow.Flow

interface AIEngine<I, O> {
    val name: String
    fun process(input: I): Flow<O>
}

interface ReasoningEngine : AIEngine<String, ReasoningResult>

data class ReasoningResult(
    val thought: String,
    val action: String,
    val confidence: Float
)

interface MemoryEngine {
    suspend fun store(key: String, value: Any)
    suspend fun retrieve(key: String): Any?
    fun observe(key: String): Flow<Any?>
}

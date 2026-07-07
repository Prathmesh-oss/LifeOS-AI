package com.lifeosai.app.ai.reasoning

import kotlinx.coroutines.flow.Flow

interface ReasoningEngine {
    fun reason(prompt: String): Flow<String>
}

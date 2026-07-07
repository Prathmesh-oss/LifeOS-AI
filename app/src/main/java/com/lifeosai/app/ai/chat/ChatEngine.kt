package com.lifeosai.app.ai.chat

import com.lifeosai.app.ai.core.model.AIResult
import kotlinx.coroutines.flow.Flow

interface ChatEngine {
    /**
     * Handles multi-turn conversations with history and context awareness.
     */
    fun streamResponse(message: String, history: List<ChatMessage>): Flow<AIResult<String>>
}

data class ChatMessage(
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageRole {
    USER, AI, SYSTEM
}

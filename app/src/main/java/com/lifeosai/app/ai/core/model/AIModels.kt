package com.lifeosai.app.ai.core.model

import java.time.LocalDateTime

data class AIContext(
    val userDnaId: String,
    val currentTimestamp: LocalDateTime = LocalDateTime.now(),
    val activeFocusMode: Boolean = false,
    val recentMemoryIds: List<String> = emptyList()
)

sealed interface AIResult<out T> {
    data class Success<T>(val data: T) : AIResult<T>
    data class Error(val message: String, val throwable: Throwable? = null) : AIResult<Nothing>
    data object Processing : AIResult<Nothing>
}

data class AISuggestion(
    val id: String,
    val title: String,
    val description: String,
    val priority: Int,
    val type: SuggestionType
)

enum class SuggestionType {
    ACTION, INSIGHT, WARNING, ROUTINE
}

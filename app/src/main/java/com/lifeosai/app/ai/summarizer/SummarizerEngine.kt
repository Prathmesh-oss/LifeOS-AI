package com.lifeosai.app.ai.summarizer

interface SummarizerEngine {
    /**
     * Summarizes long contexts into concise insights.
     */
    suspend fun summarize(content: String, length: SummaryLength = SummaryLength.MEDIUM): String
}

enum class SummaryLength {
    SHORT, MEDIUM, LONG
}

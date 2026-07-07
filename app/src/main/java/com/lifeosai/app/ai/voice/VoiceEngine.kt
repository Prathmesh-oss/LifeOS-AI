package com.lifeosai.app.ai.voice

import kotlinx.coroutines.flow.Flow

interface VoiceEngine {
    /**
     * Converts Speech to Text in real-time.
     */
    fun speechToText(): Flow<SpeechState>

    /**
     * Converts Text to Speech.
     */
    suspend fun textToSpeech(text: String)
}

sealed interface SpeechState {
    data object Listening : SpeechState
    data class Result(val text: String) : SpeechState
    data class Error(val message: String) : SpeechState
}

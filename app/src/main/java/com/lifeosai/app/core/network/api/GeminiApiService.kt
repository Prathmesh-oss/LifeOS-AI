package com.lifeosai.app.core.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Dedicated service for Google's Gemini Pro API.
 * This provides the "Reasoning" capabilities of LifeOS AI.
 */
interface GeminiApiService {

    @POST("v1beta/models/gemini-1.5-pro:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}

data class GeminiRequest(val contents: List<Content>)
data class Content(val parts: List<Part>)
data class Part(val text: String)

data class GeminiResponse(val candidates: List<Candidate>)
data class Candidate(val content: Content)

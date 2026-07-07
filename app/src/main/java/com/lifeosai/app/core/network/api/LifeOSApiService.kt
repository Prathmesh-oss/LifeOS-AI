package com.lifeosai.app.core.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The primary API service for LifeOS AI.
 * Handles tasks, user profile sync, and DNA evolution telemetry.
 */
interface LifeOSApiService {

    @GET("v1/user/profile")
    suspend fun getUserProfile(): Response<UserProfileDto>

    @GET("v1/tasks")
    suspend fun getTasks(): Response<List<TaskDto>>

    @GET("v1/dna/status")
    suspend fun getDnaStatus(): Response<DnaStatusDto>
}

// Minimal DTOs for compilation. In a real project, these are complex.
data class UserProfileDto(val id: String, val name: String)
data class TaskDto(val id: String, val title: String)
data class DnaStatusDto(val score: Int, val type: String)

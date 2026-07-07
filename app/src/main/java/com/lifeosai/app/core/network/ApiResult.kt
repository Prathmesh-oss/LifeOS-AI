package com.lifeosai.app.core.network

import kotlinx.serialization.Serializable

/**
 * A sealed interface representing the result of a network request.
 * This is the production-standard way to handle API responses, ensuring
 * that the domain layer never receives raw exceptions.
 */
sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(val error: NetworkError) : ApiResult<Nothing>
    data object Loading : ApiResult<Nothing>
}

/**
 * Robust error classification for LifeOS AI.
 * Handles HTTP errors, connectivity issues, and parsing failures.
 */
@Serializable
sealed interface NetworkError {
    data object Connectivity : NetworkError     // No internet connection
    data object Timeout : NetworkError          // Request timed out
    data object Server : NetworkError           // 5xx errors
    data object Unauthorized : NetworkError     // 401 errors
    data object Forbidden : NetworkError        // 403 errors
    data object NotFound : NetworkError         // 404 errors
    data object RateLimit : NetworkError        // 429 errors
    data object Serialization : NetworkError    // JSON parsing failure
    data object Unknown : NetworkError          // Unexpected issues
    data class Custom(val message: String, val code: Int) : NetworkError
}

/**
 * Extension to convert ApiResult to a safe domain result.
 */
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Error -> ApiResult.Error(error)
        is ApiResult.Loading -> ApiResult.Loading
    }
}

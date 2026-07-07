package com.lifeosai.app.core.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Base repository for safe network execution in LifeOS AI.
 * Handles exceptions and converts them into the [ApiResult] sealed interface.
 */
abstract class BaseNetworkRepository(
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return withContext(ioDispatcher) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ApiResult.Success(body)
                    } else {
                        ApiResult.Error(NetworkError.Unknown)
                    }
                } else {
                    ApiResult.Error(mapErrorCode(response.code()))
                }
            } catch (e: Exception) {
                ApiResult.Error(mapException(e))
            }
        }
    }

    private fun mapErrorCode(code: Int): NetworkError {
        return when (code) {
            401 -> NetworkError.Unauthorized
            403 -> NetworkError.Forbidden
            404 -> NetworkError.NotFound
            429 -> NetworkError.RateLimit
            in 500..599 -> NetworkError.Server
            else -> NetworkError.Unknown
        }
    }

    private fun mapException(e: Exception): NetworkError {
        return when (e) {
            is SocketTimeoutException -> NetworkError.Timeout
            is IOException -> NetworkError.Connectivity
            else -> NetworkError.Unknown
        }
    }
}

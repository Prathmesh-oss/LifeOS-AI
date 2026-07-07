package com.lifeosai.app.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Automatically retries failed requests up to a maximum limit.
 * Uses exponential backoff (conceptual) or simple retry for transient errors.
 */
@Singleton
class RetryInterceptor @Inject constructor() : Interceptor {

    private val maxRetries = 3

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            null
        }

        var tryCount = 0
        while ((response == null || !response.isSuccessful) && tryCount < maxRetries) {
            // Only retry on transient codes: 503, 504, or IOExceptions
            val shouldRetry = response == null || response.code == 503 || response.code == 504
            
            if (!shouldRetry) break

            tryCount++
            response?.close()
            
            // Wait before retry (exponential backoff would be better here)
            Thread.sleep(1000L * tryCount)
            
            response = try {
                chain.proceed(request)
            } catch (e: IOException) {
                null
            }
        }

        return response ?: chain.proceed(request) // Final attempt or original failure
    }
}

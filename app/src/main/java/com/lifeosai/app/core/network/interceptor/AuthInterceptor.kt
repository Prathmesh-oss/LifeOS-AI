package com.lifeosai.app.core.network.interceptor

import com.lifeosai.app.core.network.auth.TokenProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Injects Authorization headers into network requests.
 * Uses runBlocking to wait for the flow's first value, as OkHttp is synchronous.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val NO_AUTH_HEADER = "No-Authentication: true"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Skip auth if header is present
        if (request.header("No-Authentication") != null) {
            return chain.proceed(request.newBuilder().removeHeader("No-Authentication").build())
        }

        val token = runBlocking {
            tokenProvider.accessToken().first()
        }

        return if (!token.isNullOrBlank()) {
            val authenticatedRequest = request.newBuilder()
                .header(HEADER_AUTHORIZATION, "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}

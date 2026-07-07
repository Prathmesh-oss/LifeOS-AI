package com.lifeosai.app.core.network.auth

import kotlinx.coroutines.flow.Flow

/**
 * Contract for providing authentication tokens to the networking layer.
 * Usually implemented by the Identity module or DataStore.
 */
interface TokenProvider {
    /**
     * Returns a Flow of the current access token.
     */
    fun accessToken(): Flow<String?>

    /**
     * Clears tokens (e.g. on logout).
     */
    suspend fun clearTokens()

    /**
     * Logic for refreshing tokens when a 401 occurs.
     */
    suspend fun refreshToken(): String?
}

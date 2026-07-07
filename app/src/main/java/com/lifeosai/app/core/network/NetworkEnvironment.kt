package com.lifeosai.app.core.network

import com.lifeosai.app.BuildConfig

/**
 * Manages API environments for LifeOS AI.
 * Ensures we can switch between development, staging, and production securely.
 */
enum class NetworkEnvironment(
    val baseUrl: String,
    val geminiUrl: String,
    val timeoutSeconds: Long = 30L
) {
    DEVELOPMENT(
        baseUrl = "https://dev-api.lifeosai.com/",
        geminiUrl = "https://generativelanguage.googleapis.com/"
    ),
    STAGING(
        baseUrl = "https://staging-api.lifeosai.com/",
        geminiUrl = "https://generativelanguage.googleapis.com/"
    ),
    PRODUCTION(
        baseUrl = "https://api.lifeosai.com/",
        geminiUrl = "https://generativelanguage.googleapis.com/",
        timeoutSeconds = 60L // Production usually has higher load/tighter timeouts
    );

    companion object {
        /**
         * Automatically determines environment based on Build Flavor or Type.
         * For this project, we default to DEVELOPMENT unless explicitly configured.
         */
        fun current(): NetworkEnvironment {
            return if (BuildConfig.DEBUG) {
                DEVELOPMENT
            } else {
                PRODUCTION
            }
        }
    }
}

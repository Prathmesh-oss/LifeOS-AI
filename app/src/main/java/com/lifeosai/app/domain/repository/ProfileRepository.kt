package com.lifeosai.app.domain.repository

import com.lifeosai.app.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing user profile, DNA, analytics, and settings.
 * Resides in the domain layer to maintain Clean Architecture boundaries.
 */
interface ProfileRepository {
    /**
     * Observes the user's profile data.
     */
    fun observeProfile(): Flow<UserProfile>

    /**
     * Updates the AI personality setting.
     */
    suspend fun updateAIPersonality(personality: AIPersonality)

    /**
     * Updates appearance settings.
     */
    suspend fun updateAppearance(settings: AppearanceSettings)

    /**
     * Updates notification settings.
     */
    suspend fun updateNotifications(settings: NotificationSettings)

    /**
     * Updates security settings.
     */
    suspend fun updateSecurity(settings: SecuritySettings)

    /**
     * Triggers a manual backup.
     */
    suspend fun triggerBackup(target: BackupTarget)

    /**
     * Logs out the user.
     */
    suspend fun logout()
}

enum class BackupTarget {
    GOOGLE_DRIVE, LOCAL
}

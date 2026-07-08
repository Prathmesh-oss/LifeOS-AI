package com.lifeosai.app.data.repository

import com.lifeosai.app.domain.model.*
import com.lifeosai.app.domain.repository.BackupTarget
import com.lifeosai.app.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {

    private val _profile = MutableStateFlow(createMockProfile())

    override fun observeProfile(): Flow<UserProfile> = _profile.asStateFlow()

    override suspend fun updateAIPersonality(personality: AIPersonality) {
        _profile.update { it.copy(settings = it.settings.copy(aiPersonality = personality)) }
    }

    override suspend fun updateAppearance(settings: AppearanceSettings) {
        _profile.update { it.copy(settings = it.settings.copy(appearance = settings)) }
    }

    override suspend fun updateNotifications(settings: NotificationSettings) {
        _profile.update { it.copy(settings = it.settings.copy(notifications = settings)) }
    }

    override suspend fun updateSecurity(settings: SecuritySettings) {
        _profile.update { it.copy(settings = it.settings.copy(security = settings)) }
    }

    override suspend fun triggerBackup(target: BackupTarget) {
        delay(2000) // Simulate network/io
    }

    override suspend fun logout() {
        delay(1000)
    }

    private fun createMockProfile() = UserProfile(
        id = "1",
        name = "Alex Rivier",
        email = "alex@lifeosai.com",
        avatarUrl = null,
        joinDate = LocalDate.of(2024, 1, 15),
        subscription = SubscriptionPlan.PRO,
        dna = LifeOSDNAExtended(
            productivityType = DNATrait(
                "Productivity Type", 
                "Deep Worker", 
                "You thrive in long, uninterrupted blocks of concentration where you can tackle complex problems."
            ),
            focusType = DNATrait(
                "Focus Type", 
                "Laser Focused", 
                "Once you enter flow, external distractions become virtually non-existent for you."
            ),
            thinkingStyle = DNATrait(
                "Thinking Style", 
                "Architectural", 
                "You build mental models of systems and prefer top-down approaches to problem-solving."
            ),
            learningStyle = DNATrait(
                "Learning Style", 
                "Synthesizer", 
                "You learn best by connecting disparate pieces of information into a cohesive whole."
            ),
            decisionStyle = DNATrait(
                "Decision Style", 
                "Analytical", 
                "You rely on data and logical frameworks rather than intuition for critical choices."
            )
        ),
        analytics = AIAnalytics(
            focusScore = 92,
            weeklyProgress = 0.78f,
            completionRate = 0.85f,
            habitScore = 88,
            learningScore = 95
        ),
        achievements = listOf(
            Achievement("1", "Daily Streak", "14 Days", "🔥"),
            Achievement("2", "Best Focus Day", "6.5 Hours", "🎯"),
            Achievement("3", "Hours Saved", "128h", "⏳"),
            Achievement("4", "AI Actions", "1,240", "🤖")
        ),
        settings = ProfileSettings(
            aiPersonality = AIPersonality.STRATEGIST,
            appearance = AppearanceSettings(
                isDarkMode = true,
                isAmoled = true,
                accentColor = "#BB86FC",
                isDynamicColor = true
            ),
            notifications = NotificationSettings(
                isDeepWorkModeEnabled = true,
                isSmartNotificationsEnabled = true,
                isReminderEngineEnabled = true
            ),
            security = SecuritySettings(
                isBiometricLockEnabled = true,
                isEncryptionEnabled = true
            )
        )
    )
}

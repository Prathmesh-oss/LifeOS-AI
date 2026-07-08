package com.lifeosai.app.domain.model

import java.time.LocalDate

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val joinDate: LocalDate,
    val subscription: SubscriptionPlan,
    val dna: LifeOSDNAExtended,
    val analytics: AIAnalytics,
    val achievements: List<Achievement>,
    val settings: ProfileSettings
)

enum class SubscriptionPlan {
    FREE, PRO, ENTERPRISE
}

data class LifeOSDNAExtended(
    val productivityType: DNATrait,
    val focusType: DNATrait,
    val thinkingStyle: DNATrait,
    val learningStyle: DNATrait,
    val decisionStyle: DNATrait
)

data class DNATrait(
    val name: String,
    val value: String,
    val aiExplanation: String
)

data class AIAnalytics(
    val focusScore: Int,
    val weeklyProgress: Float, // 0.0 to 1.0
    val completionRate: Float,
    val habitScore: Int,
    val learningScore: Int
)

data class Achievement(
    val id: String,
    val title: String,
    val value: String,
    val icon: String // Emoji or icon name
)

data class ProfileSettings(
    val aiPersonality: AIPersonality,
    val appearance: AppearanceSettings,
    val notifications: NotificationSettings,
    val security: SecuritySettings
)

enum class AIPersonality {
    COACH, MENTOR, ASSISTANT, STRATEGIST, MINIMALIST
}

data class AppearanceSettings(
    val isDarkMode: Boolean,
    val isAmoled: Boolean,
    val accentColor: String,
    val isDynamicColor: Boolean
)

data class NotificationSettings(
    val isDeepWorkModeEnabled: Boolean,
    val isSmartNotificationsEnabled: Boolean,
    val isReminderEngineEnabled: Boolean
)

data class SecuritySettings(
    val isBiometricLockEnabled: Boolean,
    val isEncryptionEnabled: Boolean
)

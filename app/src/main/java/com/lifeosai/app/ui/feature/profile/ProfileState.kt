package com.lifeosai.app.ui.feature.profile

import androidx.compose.runtime.Immutable
import com.lifeosai.app.domain.model.UserProfile
import com.lifeosai.app.domain.repository.BackupTarget

@Immutable
sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    
    data class Success(
        val userProfile: UserProfile,
        val isBackingUp: Boolean = false,
        val isLoggingOut: Boolean = false
    ) : ProfileUiState

    data class Error(val message: String) : ProfileUiState
}

sealed interface ProfileUiEvent {
    data object OnLogoutClick : ProfileUiEvent
    data class OnBackupClick(val target: BackupTarget) : ProfileUiEvent
    data class OnAIPersonalityChange(val personality: com.lifeosai.app.domain.model.AIPersonality) : ProfileUiEvent
    data class OnDarkModeToggle(val enabled: Boolean) : ProfileUiEvent
    data class OnAmoledToggle(val enabled: Boolean) : ProfileUiEvent
    data class OnDynamicColorToggle(val enabled: Boolean) : ProfileUiEvent
    data class OnDeepWorkModeToggle(val enabled: Boolean) : ProfileUiEvent
    data class OnSmartNotificationsToggle(val enabled: Boolean) : ProfileUiEvent
    data class OnReminderEngineToggle(val enabled: Boolean) : ProfileUiEvent
    data class OnBiometricToggle(val enabled: Boolean) : ProfileUiEvent
    data class OnEncryptionToggle(val enabled: Boolean) : ProfileUiEvent
}

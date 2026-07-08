package com.lifeosai.app.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeosai.app.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _isBackingUp = MutableStateFlow(false)
    private val _isLoggingOut = MutableStateFlow(false)

    val uiState: StateFlow<ProfileUiState> = combine(
        repository.observeProfile(),
        _isBackingUp,
        _isLoggingOut
    ) { profile, backingUp, loggingOut ->
        ProfileUiState.Success(
            userProfile = profile,
            isBackingUp = backingUp,
            isLoggingOut = loggingOut
        ) as ProfileUiState
    }.catch { e ->
        emit(ProfileUiState.Error(e.message ?: "Failed to load profile"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState.Loading
    )

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnAIPersonalityChange -> {
                viewModelScope.launch { repository.updateAIPersonality(event.personality) }
            }
            is ProfileUiEvent.OnBackupClick -> {
                viewModelScope.launch {
                    _isBackingUp.value = true
                    repository.triggerBackup(event.target)
                    _isBackingUp.value = false
                }
            }
            is ProfileUiEvent.OnBiometricToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.security
                current?.let {
                    viewModelScope.launch {
                        repository.updateSecurity(it.copy(isBiometricLockEnabled = event.enabled))
                    }
                }
            }
            is ProfileUiEvent.OnDarkModeToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.appearance
                current?.let {
                    viewModelScope.launch {
                        repository.updateAppearance(it.copy(isDarkMode = event.enabled))
                    }
                }
            }
            is ProfileUiEvent.OnAmoledToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.appearance
                current?.let {
                    viewModelScope.launch {
                        repository.updateAppearance(it.copy(isAmoled = event.enabled))
                    }
                }
            }
            is ProfileUiEvent.OnDynamicColorToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.appearance
                current?.let {
                    viewModelScope.launch {
                        repository.updateAppearance(it.copy(isDynamicColor = event.enabled))
                    }
                }
            }
            is ProfileUiEvent.OnDeepWorkModeToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.notifications
                current?.let {
                    viewModelScope.launch {
                        repository.updateNotifications(it.copy(isDeepWorkModeEnabled = event.enabled))
                    }
                }
            }
            is ProfileUiEvent.OnSmartNotificationsToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.notifications
                current?.let {
                    viewModelScope.launch {
                        repository.updateNotifications(it.copy(isSmartNotificationsEnabled = event.enabled))
                    }
                }
            }
            is ProfileUiEvent.OnReminderEngineToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.notifications
                current?.let {
                    viewModelScope.launch {
                        repository.updateNotifications(it.copy(isReminderEngineEnabled = event.enabled))
                    }
                }
            }
            is ProfileUiEvent.OnEncryptionToggle -> {
                val current = (uiState.value as? ProfileUiState.Success)?.userProfile?.settings?.security
                current?.let {
                    viewModelScope.launch {
                        repository.updateSecurity(it.copy(isEncryptionEnabled = event.enabled))
                    }
                }
            }
            ProfileUiEvent.OnLogoutClick -> {
                viewModelScope.launch {
                    _isLoggingOut.value = true
                    repository.logout()
                    _isLoggingOut.value = false
                }
            }
        }
    }
}

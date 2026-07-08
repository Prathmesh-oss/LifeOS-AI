package com.lifeosai.app.ui.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifeosai.app.domain.model.AIPersonality
import com.lifeosai.app.domain.repository.BackupTarget
import com.lifeosai.app.ui.designsystem.components.LifeOSEmptyState
import com.lifeosai.app.ui.designsystem.components.LifeOSLoading
import com.lifeosai.app.ui.feature.profile.components.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState) {
                is ProfileUiState.Loading -> LifeOSLoading()
                is ProfileUiState.Error -> LifeOSEmptyState(
                    title = "Profile Sync Error",
                    description = state.message
                )
                is ProfileUiState.Success -> {
                    ProfileContent(state, viewModel::onEvent)
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState.Success,
    onEvent: (ProfileUiEvent) -> Unit
) {
    val profile = state.userProfile

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // 1. Header
        item { ProfileHeader(profile) }

        // 2. DNA Section
        item { DNASection(profile.dna) }

        // 3. Analytics
        item { AnalyticsSection(profile.analytics) }

        // 4. Achievements
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    profile.achievements.forEach { achievement ->
                        AchievementItem(achievement)
                    }
                }
            }
        }

        // 5. Subscription
        item { SubscriptionCard(profile.subscription) }

        // 6. AI Personality
        item {
            SettingsSection("AI Personality") {
                AIPersonalityPicker(
                    current = profile.settings.aiPersonality,
                    onSelected = { onEvent(ProfileUiEvent.OnAIPersonalityChange(it)) }
                )
            }
        }

        // 7. Backup & Sync
        item {
            SettingsSection("Backup & Sync") {
                SettingActionItem(
                    title = "Google Drive",
                    description = "Last synced 2h ago",
                    icon = Icons.Rounded.CloudSync,
                    onClick = { onEvent(ProfileUiEvent.OnBackupClick(BackupTarget.GOOGLE_DRIVE)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingActionItem(
                    title = "Local Backup",
                    description = "Download your memory archive",
                    icon = Icons.Rounded.Storage,
                    onClick = { onEvent(ProfileUiEvent.OnBackupClick(BackupTarget.LOCAL)) }
                )
            }
        }

        // 8. Appearance
        item {
            SettingsSection("Appearance") {
                SettingToggleItem(
                    title = "Dark Mode",
                    description = "System-wide dark theme",
                    icon = Icons.Rounded.DarkMode,
                    checked = profile.settings.appearance.isDarkMode,
                    onCheckedChange = { onEvent(ProfileUiEvent.OnDarkModeToggle(it)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingToggleItem(
                    title = "AMOLED",
                    description = "Pure black for OLED screens",
                    icon = Icons.Rounded.Contrast,
                    checked = profile.settings.appearance.isAmoled,
                    onCheckedChange = { onEvent(ProfileUiEvent.OnAmoledToggle(it)) }
                )
            }
        }

        // 9. Security
        item {
            SettingsSection("Security") {
                SettingToggleItem(
                    title = "Biometric Lock",
                    description = "Face or Fingerprint required",
                    icon = Icons.Rounded.Fingerprint,
                    checked = profile.settings.security.isBiometricLockEnabled,
                    onCheckedChange = { onEvent(ProfileUiEvent.OnBiometricToggle(it)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingToggleItem(
                    title = "End-to-End Encryption",
                    description = "Your memories are private",
                    icon = Icons.Rounded.EnhancedEncryption,
                    checked = profile.settings.security.isEncryptionEnabled,
                    onCheckedChange = { onEvent(ProfileUiEvent.OnEncryptionToggle(it)) }
                )
            }
        }

        // 10. Notifications
        item {
            SettingsSection("Notifications") {
                SettingToggleItem(
                    title = "Deep Work Mode",
                    description = "Silence distractions automatically",
                    icon = Icons.Rounded.NotificationsPaused,
                    checked = profile.settings.notifications.isDeepWorkModeEnabled,
                    onCheckedChange = { onEvent(ProfileUiEvent.OnDeepWorkModeToggle(it)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingToggleItem(
                    title = "Smart Notifications",
                    description = "AI-timed reminders",
                    icon = Icons.Rounded.AutoFixHigh,
                    checked = profile.settings.notifications.isSmartNotificationsEnabled,
                    onCheckedChange = { onEvent(ProfileUiEvent.OnSmartNotificationsToggle(it)) }
                )
            }
        }

        // 11. About & Legal
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "LifeOS AI v1.0.4",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Button(
                    onClick = { onEvent(ProfileUiEvent.OnLogoutClick) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    shape = CircleShape
                ) {
                    Text("Logout", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AchievementItem(achievement: com.lifeosai.app.domain.model.Achievement) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = achievement.icon,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = achievement.value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
        )
        Text(
            text = achievement.title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AIPersonalityPicker(
    current: AIPersonality,
    onSelected: (AIPersonality) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AIPersonality.entries.forEach { personality ->
            FilterChip(
                selected = current == personality,
                onClick = { onSelected(personality) },
                label = { Text(personality.name.lowercase().replaceFirstChar { it.uppercase() }) },
                shape = CircleShape
            )
        }
    }
}

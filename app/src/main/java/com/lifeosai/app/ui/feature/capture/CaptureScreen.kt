package com.lifeosai.app.ui.feature.capture

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifeosai.app.ui.designsystem.components.LifeOSEmptyState
import com.lifeosai.app.ui.designsystem.components.LifeOSLoading
import com.lifeosai.app.ui.feature.capture.components.*

@Composable
fun CaptureScreen(
    viewModel: CaptureViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.onEvent(CaptureUiEvent.OnVoiceCaptureClick) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Rounded.Mic, contentDescription = "Voice Capture", modifier = Modifier.size(32.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState) {
                is CaptureUiState.Loading -> LifeOSLoading()
                is CaptureUiState.Error -> LifeOSEmptyState(
                    title = "Capture System Error",
                    description = state.message
                )
                is CaptureUiState.Success -> {
                    CaptureContent(state, viewModel::onEvent)
                }
            }
        }
    }
}

@Composable
private fun CaptureContent(
    state: CaptureUiState.Success,
    onEvent: (CaptureUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // 1. Title
        item {
            Text(
                text = "Capture",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
        }

        // 2. Quick Capture Card
        item {
            QuickCaptureCard(
                content = state.currentCaptureContent,
                onContentChange = { onEvent(CaptureUiEvent.OnContentChange(it)) },
                onSubmit = { onEvent(CaptureUiEvent.OnCaptureSubmit) },
                isCapturing = state.isCapturing
            )
        }

        // 3. Quick Actions Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionChip("Photo", Icons.Rounded.PhotoCamera, { onEvent(CaptureUiEvent.OnPhotoCaptureClick) })
                    ActionChip("Scan", Icons.Rounded.DocumentScanner, { onEvent(CaptureUiEvent.OnCameraScanClick) })
                    ActionChip("Import", Icons.Rounded.CloudUpload, { onEvent(CaptureUiEvent.OnDocumentImportClick) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionChip("Task", Icons.Rounded.CheckCircle, { onEvent(CaptureUiEvent.OnQuickTaskClick) })
                    ActionChip("Reminder", Icons.Rounded.NotificationAdd, { onEvent(CaptureUiEvent.OnQuickReminderClick) })
                    ActionChip("Event", Icons.Rounded.Event, { onEvent(CaptureUiEvent.OnQuickEventClick) })
                }
            }
        }

        // 4. AI Suggestions
        if (state.suggestions.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "AI Suggestions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.suggestions) { suggestion ->
                            SuggestionCard(
                                suggestion = suggestion,
                                onClick = { onEvent(CaptureUiEvent.OnSuggestionClick(suggestion.id)) }
                            )
                        }
                    }
                }
            }
        }

        // 5. Recent Captures
        if (state.recentCaptures.isNotEmpty()) {
            item {
                Text(
                    text = "Recent",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            items(state.recentCaptures) { memory ->
                RecentCaptureCard(
                    memory = memory,
                    onFavoriteToggle = { onEvent(CaptureUiEvent.OnFavoriteToggle(memory.id)) },
                    onClick = { /* Navigate to Detail */ }
                )
            }
        }

        // 6. Favorites
        if (state.favoriteCaptures.isNotEmpty()) {
            item {
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            items(state.favoriteCaptures) { memory ->
                RecentCaptureCard(
                    memory = memory,
                    onFavoriteToggle = { onEvent(CaptureUiEvent.OnFavoriteToggle(memory.id)) },
                    onClick = { /* Navigate to Detail */ }
                )
            }
        }
    }
}

@Composable
private fun ActionChip(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = null
    )
}

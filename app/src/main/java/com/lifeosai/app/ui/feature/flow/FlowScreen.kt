package com.lifeosai.app.ui.feature.flow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifeosai.app.ui.designsystem.components.LifeOSEmptyState
import com.lifeosai.app.ui.designsystem.components.LifeOSLoading
import com.lifeosai.app.ui.feature.flow.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowScreen(
    viewModel: FlowViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.onEvent(FlowUiEvent.OnVoiceAssistantClick) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Rounded.Mic, contentDescription = "Voice Assistant", modifier = Modifier.size(32.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState) {
                is FlowUiState.Loading -> LifeOSLoading()
                is FlowUiState.Error -> LifeOSEmptyState(
                    title = "Flow System Error",
                    description = state.message
                )
                is FlowUiState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.onEvent(FlowUiEvent.OnRefresh) }
                    ) {
                        FlowContent(state, viewModel::onEvent)
                    }
                }
            }
        }
    }
}

@Composable
private fun FlowContent(
    state: FlowUiState.Success,
    onEvent: (FlowUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // 1. Dynamic Greeting
        item {
            Text(
                text = state.greeting,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
        }

        // 2. Current Session or Start New
        item {
            if (state.currentSession != null) {
                FocusSessionCard(
                    session = state.currentSession,
                    onPause = { onEvent(FlowUiEvent.OnPauseSession) },
                    onResume = { onEvent(FlowUiEvent.OnResumeSession) },
                    onStop = { onEvent(FlowUiEvent.OnStopSession) }
                )
            } else {
                StartSessionCard(onStart = { name, duration -> 
                    onEvent(FlowUiEvent.OnStartSession(name, duration)) 
                })
            }
        }

        // 3. Stats Section
        item {
            StatisticsCard(stats = state.stats)
        }

        // 4. AI Recommendations
        if (state.recommendations.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "AI Recommendations",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    state.recommendations.forEach { recommendation ->
                        RecommendationCard(
                            recommendation = recommendation,
                            onClick = { onEvent(FlowUiEvent.OnRecommendationClick(recommendation.id)) }
                        )
                    }
                }
            }
        }

        // 5. Phone Silence Status
        item {
            SilenceStatusCard(
                isSilent = state.isPhoneSilent,
                onToggle = { onEvent(FlowUiEvent.OnToggleSilence) }
            )
        }

        // 6. Focus Music & Ambient
        item {
            FocusMusicSection()
        }

        // 7. History
        if (state.history.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Sessions",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            items(state.history) { session ->
                HistoryCard(session = session)
            }
        }
    }
}

@Composable
private fun StartSessionCard(onStart: (String, Int) -> Unit) {
    Surface(
        onClick = { onStart("Deep Work", 25) },
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Rounded.Bolt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Enter Flow State",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Start a 25-minute deep work session",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onStart("Deep Work", 25) },
                shape = CircleShape
            ) {
                Text("Start Focus Session")
            }
        }
    }
}

@Composable
private fun SilenceStatusCard(isSilent: Boolean, onToggle: () -> Unit) {
    Surface(
        onClick = onToggle,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isSilent) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications,
                contentDescription = null,
                tint = if (isSilent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isSilent) "Distractions Silenced" else "Notifications On",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isSilent) "Your phone is in Focus Mode." else "Turn on to minimize distractions.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = isSilent, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
private fun FocusMusicSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Focus Soundscape",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item { SoundChip("Binaural Beats", Icons.Rounded.SurroundSound) }
            item { SoundChip("Rain", Icons.Rounded.Cloud) }
            item { SoundChip("Lo-Fi", Icons.Rounded.MusicNote) }
            item { SoundChip("White Noise", Icons.Rounded.Grain) }
        }
    }
}

@Composable
private fun SoundChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        onClick = { },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

package com.lifeosai.app.ui.feature.brain

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
import com.lifeosai.app.ui.designsystem.components.*
import com.lifeosai.app.ui.feature.brain.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrainScreen(
    viewModel: BrainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.onEvent(BrainUiEvent.OnVoiceAssistantClick) },
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
                is BrainUiState.Loading -> LifeOSLoading()
                is BrainUiState.Error -> LifeOSEmptyState(
                    title = "Sync Interrupted",
                    description = state.message
                )
                is BrainUiState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.onEvent(BrainUiEvent.OnRefresh) }
                    ) {
                        BrainContent(state, viewModel::onEvent)
                    }
                }
            }
        }
    }
}

@Composable
private fun BrainContent(
    state: BrainUiState.Success,
    onEvent: (BrainUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Dynamic Greeting & Search
        item {
            HeaderSection(state.greeting, state.searchQuery, onEvent)
        }

        if (state.isSearching) {
            // Search Results
            items(state.searchResults) { memory ->
                MemoryCard(
                    memory = memory,
                    onClick = { onEvent(BrainUiEvent.OnMemoryClick(memory.id)) },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        } else {
            // 3. AI Chat Card
            item {
                ResumeChatCard()
            }

            // 7. Memory Statistics
            item {
                StatsSection(state.stats)
            }

            // 6. AI Insights
            if (state.insights.isNotEmpty()) {
                item {
                    InsightsSection(state.insights, onEvent)
                }
            }

            // 5. Knowledge Collections
            item {
                CollectionsSection(state.stats, onEvent)
            }

            // 8. Smart Filters
            item {
                FilterSection()
            }

            // 4. Recent Memories (Timeline)
            state.groupedMemories.forEach { (group, memories) ->
                item {
                    Text(
                        text = group,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                items(memories) { memory ->
                    MemoryCard(
                        memory = memory,
                        onClick = { onEvent(BrainUiEvent.OnMemoryClick(memory.id)) },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    greeting: String,
    searchQuery: String,
    onEvent: (BrainUiEvent) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = greeting,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LifeOSSearchBar(
            query = searchQuery,
            onQueryChange = { onEvent(BrainUiEvent.OnSearchQueryChange(it)) },
            modifier = Modifier.padding(horizontal = 24.dp),
            placeholder = "Search memories, notes, chats..."
        )
    }
}

@Composable
private fun ResumeChatCard() {
    LifeOSCard(
        onClick = { /* Navigate to Chat */ },
        modifier = Modifier.padding(horizontal = 24.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Resume Conversation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Last active 2h ago",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun StatsSection(stats: com.lifeosai.app.domain.repository.BrainStats) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem("Memories", stats.totalMemories.toString())
        StatItem("Chats", stats.aiConversations.toString())
        StatItem("Voice", stats.voiceNotes.toString())
        StatItem("Docs", stats.documents.toString())
    }
}

@Composable
private fun InsightsSection(
    insights: List<com.lifeosai.app.domain.repository.AIInsight>,
    onEvent: (BrainUiEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "AI Insights",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(insights) { insight ->
                InsightCard(
                    insight = insight,
                    onClick = { onEvent(BrainUiEvent.OnInsightClick(insight.id)) },
                    modifier = Modifier.width(280.dp)
                )
            }
        }
    }
}

@Composable
private fun CollectionsSection(
    stats: com.lifeosai.app.domain.repository.BrainStats,
    onEvent: (BrainUiEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Knowledge Collections",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { CollectionCard("Ideas", Icons.Rounded.Lightbulb, 42, { onEvent(BrainUiEvent.OnSearchQueryChange("Ideas")) }) }
            item { CollectionCard("Projects", Icons.Rounded.Inventory2, 12, { onEvent(BrainUiEvent.OnSearchQueryChange("Projects")) }) }
            item { CollectionCard("Documents", Icons.Rounded.Description, stats.documents, { onEvent(BrainUiEvent.OnSearchQueryChange("Documents")) }) }
            item { CollectionCard("Voice Notes", Icons.Rounded.Mic, stats.voiceNotes, { onEvent(BrainUiEvent.OnSearchQueryChange("Voice")) }) }
        }
    }
}

@Composable
private fun FilterSection() {
    val filters = listOf("All", "Ideas", "Voice", "Tasks", "Projects")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = filter == "All",
                onClick = { },
                label = { Text(filter) },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

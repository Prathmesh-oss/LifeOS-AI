package com.lifeosai.app.ui.feature.flow.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lifeosai.app.domain.repository.FlowStats
import com.lifeosai.app.ui.designsystem.components.LifeOSCard

@Composable
fun StatisticsCard(
    stats: FlowStats,
    modifier: Modifier = Modifier
) {
    LifeOSCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Today's Deep Work",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "Focused", value = "${stats.totalFocusedHours}h")
                StatItem(label = "Sessions", value = stats.sessionsCompleted.toString())
                StatItem(label = "Distractions", value = stats.totalDistractions.toString())
                StatItem(label = "Score", value = stats.averageFocusScore.toString())
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

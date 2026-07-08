package com.lifeosai.app.ui.feature.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lifeosai.app.domain.model.AIAnalytics
import com.lifeosai.app.ui.designsystem.components.LifeOSCard

@Composable
fun AnalyticsSection(
    analytics: AIAnalytics,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "AI Growth Analytics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AnalyticsItem(
                label = "Focus Score",
                value = analytics.focusScore.toString(),
                modifier = Modifier.weight(1f)
            )
            AnalyticsItem(
                label = "Progress",
                value = "${(analytics.weeklyProgress * 100).toInt()}%",
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AnalyticsItem(
                label = "Habit Score",
                value = analytics.habitScore.toString(),
                modifier = Modifier.weight(1f)
            )
            AnalyticsItem(
                label = "Learning",
                value = analytics.learningScore.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AnalyticsItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    LifeOSCard(modifier = modifier) {
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

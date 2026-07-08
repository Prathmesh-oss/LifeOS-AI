package com.lifeosai.app.ui.feature.capture.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.model.MemoryType
import com.lifeosai.app.ui.designsystem.components.LifeOSCard
import java.time.format.DateTimeFormatter

@Composable
fun RecentCaptureCard(
    memory: Memory,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LifeOSCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconForType(memory.type),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = memory.content,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${memory.type.name} • ${memory.createdAt.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (memory.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (memory.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun getIconForType(type: MemoryType): ImageVector = when (type) {
    MemoryType.IDEA -> Icons.Rounded.Lightbulb
    MemoryType.TASK -> Icons.Rounded.CheckCircle
    MemoryType.MEMORY -> Icons.Rounded.AutoAwesome
    MemoryType.PROJECT -> Icons.Rounded.Inventory2
    MemoryType.MEETING -> Icons.Rounded.Groups
    MemoryType.QUOTE -> Icons.Rounded.FormatQuote
    MemoryType.BOOKMARK -> Icons.Rounded.Bookmark
    MemoryType.VOICE -> Icons.Rounded.Mic
    MemoryType.CONVERSATION -> Icons.Rounded.ChatBubble
    MemoryType.AI_INSIGHT -> Icons.Rounded.AutoAwesome
    MemoryType.SYSTEM_LOG -> Icons.Rounded.Settings
}

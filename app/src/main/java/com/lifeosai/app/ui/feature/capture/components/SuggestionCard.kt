package com.lifeosai.app.ui.feature.capture.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lifeosai.app.domain.repository.CaptureSuggestion
import com.lifeosai.app.ui.designsystem.components.LifeOSCard

@Composable
fun SuggestionCard(
    suggestion: CaptureSuggestion,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LifeOSCard(
        onClick = onClick,
        modifier = modifier.width(260.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
        borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = suggestion.text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

package com.lifeosai.app.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.lifeosai.app.ui.designsystem.foundation.*

@Composable
fun LifeOSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    
    CompositionLocalProvider(
        LocalSpacing provides LifeOSSpacing()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

object LifeOSTheme {
    val spacing: LifeOSSpacing
        @Composable
        get() = LocalSpacing.current
}

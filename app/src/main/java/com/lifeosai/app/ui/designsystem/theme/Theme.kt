package com.lifeosai.app.ui.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.lifeosai.app.ui.designsystem.foundation.*

// Extended Semantic Colors
data class LifeOSExtendedColors(
    val aiPurple: Color,
    val deepWork: Color,
    val energy: Color,
    val focus: Color
)

private val DarkColorScheme = darkColorScheme(
    primary = Indigo500,
    secondary = Indigo200,
    tertiary = AI_Purple,
    background = Black, // AMOLED optimization
    surface = Gray950,
    onSurface = Gray100,
    onSurfaceVariant = Gray400,
    outline = Gray800
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo500,
    secondary = Indigo900,
    tertiary = AI_Purple,
    background = White,
    surface = Gray100,
    onSurface = Black,
    onSurfaceVariant = Gray400,
    outline = Gray800
)

@Composable
fun LifeOSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = LifeOSExtendedColors(
        aiPurple = AI_Purple,
        deepWork = DeepWork_Red,
        energy = Energy_Green,
        focus = Focus_Orange
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalSpacing provides LifeOSSpacing()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = LifeOSTypography,
            shapes = LifeOSShapes,
            content = content
        )
    }
}

// Accessor for custom tokens
object LifeOSTheme {
    val spacing: LifeOSSpacing
        @Composable
        get() = LocalSpacing.current
}

package com.lifeosai.app.ui.designsystem.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object LifeOSAnimation {
    // Luxury Spring - Snappy but smooth
    val SnappySpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    // Calm Transition - Used for screen entries
    val CalmTween = tween<Float>(
        durationMillis = 400
    )

    // Micro-interaction - Used for buttons
    val ButtonScale = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )
}

package com.lifeosai.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * LifeOS AI Shape System
 * 
 * Uses generous corner radii for a modern, high-end feel.
 * Follows Material 3 shape token roles.
 */

val Shapes = Shapes(
    // Smallest components like tooltips
    extraSmall = RoundedCornerShape(8.dp),
    
    // Components like buttons, small FABs
    small = RoundedCornerShape(12.dp),
    
    // Cards, medium components
    medium = RoundedCornerShape(20.dp),
    
    // Large cards, modals, sheets
    large = RoundedCornerShape(28.dp),
    
    // Dialogs, full-screen containers
    extraLarge = RoundedCornerShape(36.dp)
)

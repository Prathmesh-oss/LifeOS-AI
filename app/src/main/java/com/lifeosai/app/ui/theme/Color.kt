package com.lifeosai.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * LifeOS AI Flagship Color Palette
 * 
 * DESIGN PRINCIPLES:
 * 1. Deep Obsidian: Not pure black (#000000) but a rich, layered dark gray (#08090A) 
 *    to allow for "Negative Elevation" and reduced eye fatigue.
 * 2. Tonal Hierarchy: Surfaces are layered using Material 3 tonal elevation and 
 *    transparency rather than just lightness.
 * 3. AI Intelligence: Neural Indigo and Cyber Cyan signify artificial intelligence 
 *    and high-performance productivity.
 */

// --- Base Obsidian Scale ---
val Obsidian = Color(0xFF08090A)       // Base Background
val DeepGrey = Color(0xFF0F1113)       // Primary Surface
val ElevatedGrey = Color(0xFF16181B)   // Card Surface
val HighlightGrey = Color(0xFF1E2124)  // Elevated Highlights

// --- Brand Colors (Neural Intelligence) ---
val NeuralIndigo = Color(0xFF6366F1)        // Primary Action
val NeuralIndigoLight = Color(0xFF818CF8)   // Hover/Focus
val NeuralIndigoDark = Color(0xFF4338CA)    // Pressed
val NeuralIndigoContainer = Color(0xFF1E1B4B) // Tonal Container

val CyberCyan = Color(0xFF06B6D4)           // Tertiary / AI Accent
val CyberCyanLight = Color(0xFF22D3EE)
val CyberCyanContainer = Color(0xFF083344)

// --- Secondary (Zinc / Steel) ---
val Zinc50 = Color(0xFFFAFAFA)
val Zinc100 = Color(0xFFF4F4F5)
val Zinc200 = Color(0xFFE4E4E7)
val Zinc300 = Color(0xFFD4D4D8)
val Zinc400 = Color(0xFFA1A1AA)
val Zinc500 = Color(0xFF71717A)
val Zinc600 = Color(0xFF52525B)
val Zinc700 = Color(0xFF3F3F46)
val Zinc800 = Color(0xFF27272A)
val Zinc900 = Color(0xFF18181B)

// --- Semantic Utility ---
val SuccessEmerald = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)
val DestructiveRed = Color(0xFFEF4444)
val InfoSky = Color(0xFF0EA5E9)

// --- Material 3 Role Mappings ---

// Primary: High-emphasis actions and branding
val Primary = NeuralIndigo
val OnPrimary = Color.White
val PrimaryContainer = NeuralIndigoContainer
val OnPrimaryContainer = NeuralIndigoLight

// Secondary: Less prominent components
val Secondary = Zinc400
val OnSecondary = Obsidian
val SecondaryContainer = Zinc800
val OnSecondaryContainer = Zinc200

// Tertiary: AI features, highlights, and secondary accents
val Tertiary = CyberCyan
val OnTertiary = Obsidian
val TertiaryContainer = CyberCyanContainer
val OnTertiaryContainer = CyberCyanLight

// Background & Surface
val Background = Obsidian
val OnBackground = Zinc100

val Surface = DeepGrey
val OnSurface = Zinc100
val SurfaceVariant = ElevatedGrey
val OnSurfaceVariant = Zinc400

val Outline = Zinc700
val OutlineVariant = Zinc800

// Error
val Error = DestructiveRed
val OnError = Color.White
val ErrorContainer = Color(0xFF450A0A)
val OnErrorContainer = Color(0xFFFECACA)

// --- Premium Effects & Gradients ---
val GlassWhite = Color.White.copy(alpha = 0.05f)
val GlassBlack = Color.Black.copy(alpha = 0.40f)
val BorderGlass = Color.White.copy(alpha = 0.10f)

// AI Gradient for shimmering and breath effects
val AIGradient = listOf(
    NeuralIndigo,
    CyberCyan,
    NeuralIndigoLight
)

val SurfaceGradient = listOf(
    DeepGrey,
    ElevatedGrey
)

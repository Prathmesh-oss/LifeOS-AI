package com.lifeosai.app.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destinations for LifeOS AI.
 * Using Kotlin Serialization as per modern Jetpack Navigation standards.
 */
sealed interface Destination {
    
    @Serializable
    data object Splash : Destination

    @Serializable
    data object Onboarding : Destination

    @Serializable
    data object Authentication : Destination

    // Main App Nested Graph
    @Serializable
    data object MainGraph : Destination {
        
        @Serializable
        data object Nexus : Destination
        
        @Serializable
        data object Brain : Destination
        
        @Serializable
        data object Flow : Destination
        
        @Serializable
        data object Missions : Destination
        
        @Serializable
        data object Profile : Destination
    }

    // AI Features Nested Graph
    @Serializable
    data object AIGraph : Destination {
        
        @Serializable
        data object Chat : Destination
        
        @Serializable
        data object Memory : Destination
        
        @Serializable
        data object DNA : Destination
        
        @Serializable
        data object VoiceCapture : Destination
    }

    @Serializable
    data object Settings : Destination

    @Serializable
    data class Search(val query: String? = null) : Destination
}

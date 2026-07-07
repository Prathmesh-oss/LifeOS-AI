package com.lifeosai.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Splash : Route
    
    @Serializable
    data object Nexus : Route
    
    @Serializable
    data object Brain : Route
    
    @Serializable
    data object Capture : Route
    
    @Serializable
    data object Flow : Route
    
    @Serializable
    data object Profile : Route
}

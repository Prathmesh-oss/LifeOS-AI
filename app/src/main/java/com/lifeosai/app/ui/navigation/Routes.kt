package com.lifeosai.app.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash : Route
    
    @Serializable
    data object Onboarding : Route
    
    @Serializable
    data object Auth : Route
    
    @Serializable
    data object Nexus : Route
    
    @Serializable
    data object Brain : Route
    
    @Serializable
    data object Flow : Route
    
    @Serializable
    data object Missions : Route
    
    @Serializable
    data object Profile : Route
    
    @Serializable
    data object Settings : Route
}

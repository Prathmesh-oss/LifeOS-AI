package com.lifeosai.app.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Nexus : Screen
    
    @Serializable
    data object Brain : Screen
    
    @Serializable
    data object Flow : Screen
    
    @Serializable
    data object Missions : Screen
    
    @Serializable
    data object Profile : Screen
}

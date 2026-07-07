package com.lifeosai.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom Navigation Items for LifeOS AI.
 * Defined in the navigation layer to maintain single source of truth for routes.
 */
sealed class BottomNavItem(
    val destination: Destination,
    val icon: ImageVector,
    val label: String
) {
    data object Nexus : BottomNavItem(Destination.MainGraph.Nexus, Icons.Rounded.GridView, "Nexus")
    data object Brain : BottomNavItem(Destination.MainGraph.Brain, Icons.Rounded.Psychology, "Brain")
    data object Flow : BottomNavItem(Destination.MainGraph.Flow, Icons.Rounded.BlurOn, "Flow")
    data object Missions : BottomNavItem(Destination.MainGraph.Missions, Icons.Rounded.Flag, "Missions")
    data object Profile : BottomNavItem(Destination.MainGraph.Profile, Icons.Rounded.Person, "Profile")

    companion object {
        val items = listOf(Nexus, Brain, Flow, Missions, Profile)
    }
}

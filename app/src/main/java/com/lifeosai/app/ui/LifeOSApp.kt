package com.lifeosai.app.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifeosai.app.navigation.Route
import com.lifeosai.app.ui.feature.brain.BrainScreen
import com.lifeosai.app.ui.feature.capture.CaptureScreen
import com.lifeosai.app.ui.feature.flow.FlowScreen
import com.lifeosai.app.ui.feature.profile.ProfileScreen
import com.lifeosai.app.ui.nexus.NexusScreen
import com.lifeosai.app.ui.splash.SplashScreen

sealed class BottomNavItem<T : Route>(val route: T, val icon: ImageVector, val label: String) {
    data object Nexus : BottomNavItem<Route.Nexus>(Route.Nexus, Icons.Rounded.GridView, "Nexus")
    data object Brain : BottomNavItem<Route.Brain>(Route.Brain, Icons.Rounded.Psychology, "Brain")
    data object Capture : BottomNavItem<Route.Capture>(Route.Capture, Icons.Rounded.AddCircle, "Capture")
    data object Flow : BottomNavItem<Route.Flow>(Route.Flow, Icons.Rounded.Grain, "Flow")
    data object Profile : BottomNavItem<Route.Profile>(Route.Profile, Icons.Rounded.Person, "Profile")
}

@Composable
fun LifeOSApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.Nexus,
        BottomNavItem.Brain,
        BottomNavItem.Capture,
        BottomNavItem.Flow,
        BottomNavItem.Profile
    )

    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { dest ->
                            dest.hasRoute(item.route::class) 
                        } == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { 
                                Icon(
                                    imageVector = item.icon, 
                                    contentDescription = item.label,
                                    modifier = Modifier.size(26.dp)
                                ) 
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Route.Splash> {
                SplashScreen(onSplashFinished = {
                    navController.navigate(Route.Nexus) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                })
            }
            composable<Route.Nexus> {
                NexusScreen()
            }
            composable<Route.Brain> {
                BrainScreen()
            }
            composable<Route.Capture> {
                CaptureScreen()
            }
            composable<Route.Flow> {
                FlowScreen()
            }
            composable<Route.Profile> {
                ProfileScreen()
            }
        }
    }
}

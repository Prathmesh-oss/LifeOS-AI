package com.lifeosai.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.lifeosai.app.ui.theme.*

sealed class BottomNavItem<T : Route>(val route: T, val icon: ImageVector, val label: String) {
    data object Nexus : BottomNavItem<Route.Nexus>(Route.Nexus, Icons.Rounded.GridView, "Nexus")
    data object Brain : BottomNavItem<Route.Brain>(Route.Brain, Icons.Rounded.Psychology, "Brain")
    data object Capture : BottomNavItem<Route.Capture>(Route.Capture, Icons.Rounded.AddCircle, "Capture")
    data object Flow : BottomNavItem<Route.Flow>(Route.Flow, Icons.Rounded.Grain, "Flow")
    data object Profile : BottomNavItem<Route.Profile>(Route.Profile, Icons.Rounded.Person, "Profile")
}

@OptIn(ExperimentalAnimationApi::class)
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
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                LifeOSNavigationBar(
                    bottomNavItems = bottomNavItems,
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                AIAssistantFAB(onClick = { /* Open Voice Assistant */ })
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Route.Splash,
                modifier = Modifier.fillMaxSize(),
                enterTransition = { fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.95f) },
                exitTransition = { fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 1.05f) },
                popEnterTransition = { fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 1.05f) },
                popExitTransition = { fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.95f) }
            ) {
                composable<Route.Splash> {
                    SplashScreen(onSplashFinished = {
                        navController.navigate(Route.Nexus) {
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    })
                }
                composable<Route.Nexus> { NexusScreen() }
                composable<Route.Brain> { BrainScreen() }
                composable<Route.Capture> { CaptureScreen() }
                composable<Route.Flow> { FlowScreen() }
                composable<Route.Profile> { ProfileScreen() }
            }
            
            // Apply padding to NavHost via a wrapper if needed, 
            // but for edge-to-edge we often want content behind the bottom bar
            // and we handle padding inside the screens.
            // However, to keep it simple and correct:
            Spacer(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun LifeOSNavigationBar(
    bottomNavItems: List<BottomNavItem<*>>,
    currentDestination: androidx.navigation.NavDestination?,
    onNavigate: (Route) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(Shapes.extraLarge),
        color = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    )
                )
                .blur(20.dp) // Glass effect
        )

        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        val iconScale by animateFloatAsState(if (isSelected) 1.2f else 1f, label = "iconScale")
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(iconScale),
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun AIAssistantFAB(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "AIGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset(y = (-32).dp) // Float above the nav bar
            .size(64.dp)
            .scale(scale)
    ) {
        // Glow Effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Main FAB Button
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = "AI Assistant",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

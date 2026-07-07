package com.lifeosai.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.lifeosai.app.ui.nexus.NexusScreen
import com.lifeosai.app.ui.splash.SplashScreen

private const val DEEP_LINK_URI = "https://lifeosai.com"

/**
 * Main Navigation Graph for LifeOS AI.
 * Handles deep links, nested graphs, and premium transitions.
 */
@Composable
fun LifeOSNavHost(
    navController: NavHostController,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    // Listen to navigator events from ViewModels
    LaunchedEffect(Unit) {
        navigator.navEvents.collect { event ->
            when (event) {
                is NavEvent.To -> {
                    navController.navigate(event.destination) {
                        event.popUpTo?.let {
                            popUpTo(it) { inclusive = event.inclusive }
                        }
                    }
                }
                is NavEvent.Back -> navController.navigateUp()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Splash,
        modifier = modifier,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(400)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(400)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(400)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(400)) }
    ) {
        composable<Destination.Splash> {
            SplashScreen(onSplashFinished = {
                navigator.navigate(NavEvent.To(Destination.MainGraph.Nexus, popUpTo = Destination.Splash, inclusive = true))
            })
        }

        composable<Destination.Onboarding> { Placeholder("Onboarding") }
        composable<Destination.Authentication> { Placeholder("Auth") }

        // Main App Graph - Supports multiple backstacks for BottomNav
        navigation<Destination.MainGraph>(startDestination = Destination.MainGraph.Nexus) {
            composable<Destination.MainGraph.Nexus>(
                deepLinks = listOf(navDeepLink { uriPattern = "$DEEP_LINK_URI/nexus" })
            ) { NexusScreen() }
            
            composable<Destination.MainGraph.Brain> { Placeholder("Brain") }
            composable<Destination.MainGraph.Flow> { Placeholder("Flow") }
            composable<Destination.MainGraph.Missions> { Placeholder("Missions") }
            composable<Destination.MainGraph.Profile> { Placeholder("Profile") }
        }

        // AI Intelligence Graph
        navigation<Destination.AIGraph>(startDestination = Destination.AIGraph.Chat) {
            composable<Destination.AIGraph.Chat>(
                deepLinks = listOf(navDeepLink { uriPattern = "$DEEP_LINK_URI/chat" })
            ) { Placeholder("AI Chat") }
            
            composable<Destination.AIGraph.Memory> { Placeholder("Memory") }
            composable<Destination.AIGraph.DNA> { Placeholder("DNA") }
            composable<Destination.AIGraph.VoiceCapture> { Placeholder("Voice Capture") }
        }

        composable<Destination.Settings> { Placeholder("Settings") }
        
        composable<Destination.Search>(
            deepLinks = listOf(navDeepLink { uriPattern = "$DEEP_LINK_URI/search?q={query}" })
        ) { Placeholder("Search") }
    }
}

@Composable
private fun Placeholder(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name)
    }
}

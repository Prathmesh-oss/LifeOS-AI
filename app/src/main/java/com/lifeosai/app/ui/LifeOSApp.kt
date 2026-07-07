package com.lifeosai.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifeosai.app.navigation.BottomNavItem
import com.lifeosai.app.navigation.Destination
import com.lifeosai.app.navigation.LifeOSNavHost
import com.lifeosai.app.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun LifeOSApp(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determine if we should show the bottom bar based on current destination
    val showBottomBar = BottomNavItem.items.any { item ->
        currentDestination?.hierarchy?.any { it.hasRoute(item.destination::class) } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {
                    BottomNavItem.items.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { dest ->
                            dest.hasRoute(item.destination::class)
                        } == true
                        
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.destination) {
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
                                    modifier = Modifier.size(24.dp)
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
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = { /* Handle Quick Capture */ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = FloatingActionButtonDefaults.largeShape
                ) {
                    Icon(Icons.Rounded.Mic, contentDescription = "Quick Capture")
                }
            }
        }
    ) { innerPadding ->
        LifeOSNavHost(
            navController = navController,
            navigator = viewModel.navigator,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@HiltViewModel
class NavigationViewModel @Inject constructor(
    val navigator: Navigator
) : ViewModel()

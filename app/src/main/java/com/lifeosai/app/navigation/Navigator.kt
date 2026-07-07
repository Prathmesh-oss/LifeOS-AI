package com.lifeosai.app.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for navigating across the app from non-UI components (e.g. ViewModels).
 */
interface Navigator {
    val navEvents: kotlinx.coroutines.flow.SharedFlow<NavEvent>
    fun navigate(event: NavEvent)
}

/**
 * Sealed class representing possible navigation actions.
 */
sealed class NavEvent {
    data class To(val destination: Destination, val popUpTo: Destination? = null, val inclusive: Boolean = false) : NavEvent()
    data object Back : NavEvent()
}

@Singleton
class LifeOSNavigator @Inject constructor() : Navigator {
    private val _navEvents = MutableSharedFlow<NavEvent>(extraBufferCapacity = 1)
    override val navEvents = _navEvents.asSharedFlow()

    override fun navigate(event: NavEvent) {
        _navEvents.tryEmit(event)
    }
}

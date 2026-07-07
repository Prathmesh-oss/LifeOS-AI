package com.lifeosai.app.data.repository

import com.lifeosai.app.domain.model.*
import com.lifeosai.app.domain.repository.LifeOSRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifeOSRepositoryImpl @Inject constructor() : LifeOSRepository {

    private val _tasks = MutableStateFlow(
        listOf(
            NexusTask("1", "Finalize LifeOS AI Design System", false, "Work"),
            NexusTask("2", "Record Weekly AI Performance Review", false, "Life"),
            NexusTask("3", "Meditation Session", true, "Health")
        )
    )

    override fun getNexusData(): Flow<NexusData> = _tasks.map { tasks ->
        NexusData(
            userName = "Alex",
            dna = LifeOSDNA(
                productivityType = "Deep Work Architect",
                lifeScore = 91,
                focusEnergy = "High",
                aiStatus = "Optimized",
                peakFocusStart = "9:00 AM",
                peakFocusEnd = "11:30 AM",
                coachingStyle = "Direct & Analytical"
            ),
            insight = NexusInsight(
                text = "You have 3 important tasks today. Finishing LifeOS AI before lunch gives you a 78% chance of reaching today's goals.",
                probability = 78
            ),
            primaryMission = "Launch LifeOS AI Beta",
            tasks = tasks,
            upcomingEvents = listOf("10:00 AM - Sync with AI Engine", "2:00 PM - Deep Work Session"),
            suggestions = listOf("Take a 5-min breathwork break", "Hydrate: Drink 500ml of water", "Review tomorrow's focus")
        )
    }

    override fun getLifeOSDNA(): Flow<LifeOSDNA> = flowOf(
        LifeOSDNA(
            productivityType = "Deep Work Architect",
            lifeScore = 91,
            focusEnergy = "High",
            aiStatus = "Optimized",
            peakFocusStart = "9:00 AM",
            peakFocusEnd = "11:30 AM",
            coachingStyle = "Direct & Analytical"
        )
    )

    override suspend fun toggleTaskCompletion(taskId: String) {
        _tasks.update { tasks ->
            tasks.map {
                if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
            }
        }
    }
}

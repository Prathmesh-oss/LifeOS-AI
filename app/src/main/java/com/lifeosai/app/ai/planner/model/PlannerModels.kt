package com.lifeosai.app.ai.planner.model

import java.time.LocalDateTime

/**
 * The "Daily Flight Plan" is the output of the AI Planner.
 * It is an optimized sequence of time blocks for the day.
 */
data class FlightPlan(
    val id: String,
    val date: LocalDateTime,
    val blocks: List<ScheduledBlock>,
    val metadata: PlanMetadata
)

data class ScheduledBlock(
    val id: String,
    val title: String,
    val type: TimeBlockType,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val taskId: String? = null,
    val eventId: String? = null,
    val priority: Int,
    val isLocked: Boolean = false
)

enum class TimeBlockType {
    DEEP_WORK,
    SHALLOW_WORK,
    MEETING,
    BREAK,
    BUFFER,
    RECOVERY,
    PERSONAL
}

data class PlanMetadata(
    val efficiencyScore: Float, // 0.0 - 1.0
    val focusDensity: Float,
    val estimatedEnergyUsage: Float,
    val aiReasoning: String
)

data class PlanningContext(
    val startOfDay: LocalDateTime,
    val endOfDay: LocalDateTime,
    val currentEnergy: Float,
    val availableMinutes: Int
)

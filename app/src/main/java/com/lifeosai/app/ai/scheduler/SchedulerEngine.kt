package com.lifeosai.app.ai.scheduler

import com.lifeosai.app.ai.planner.Plan
import java.time.LocalDateTime

interface SchedulerEngine {
    /**
     * Finds the optimal time slots for a plan based on energy levels and calendar.
     */
    suspend fun schedule(plan: Plan): ScheduledPlan
}

data class ScheduledPlan(
    val planId: String,
    val timeSlots: List<TimeSlot>
)

data class TimeSlot(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val taskDescription: String
)

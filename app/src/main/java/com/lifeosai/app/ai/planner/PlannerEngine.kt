package com.lifeosai.app.ai.planner

import com.lifeosai.app.ai.core.model.AIResult
import kotlinx.coroutines.flow.Flow

interface PlannerEngine {
    /**
     * Generates a step-by-step plan for a high-level goal.
     */
    fun createPlan(goal: String): Flow<AIResult<Plan>>
}

data class Plan(
    val id: String,
    val goal: String,
    val steps: List<PlanStep>,
    val estimatedDurationMinutes: Int
)

data class PlanStep(
    val description: String,
    val priority: Int,
    val dependsOn: List<String> = emptyList()
)

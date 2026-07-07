package com.lifeosai.app.ai.planner.repository

import com.lifeosai.app.ai.planner.model.FlightPlan
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository for managing daily flight plans.
 */
interface PlannerRepository {
    
    /**
     * Observes the plan for a specific date.
     */
    fun observePlan(date: LocalDate): Flow<FlightPlan?>

    /**
     * Persists a generated flight plan.
     */
    suspend fun savePlan(plan: FlightPlan)

    /**
     * Clears historical plans.
     */
    suspend fun deletePlan(planId: String)
}

package com.lifeosai.app.ai.planner.usecase

import com.lifeosai.app.ai.planner.engine.PlannerEngine
import com.lifeosai.app.ai.planner.model.FlightPlan
import com.lifeosai.app.ai.planner.model.PlanningContext
import com.lifeosai.app.ai.planner.repository.PlannerRepository
import com.lifeosai.app.domain.model.LifeOSDNA
import com.lifeosai.app.domain.model.NexusTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * High-level Use Case to trigger the daily planning intelligence cycle.
 */
class GenerateDailyPlanUseCase @Inject constructor(
    private val engine: PlannerEngine,
    private val repository: PlannerRepository
) {
    operator fun invoke(
        tasks: List<NexusTask>,
        dna: LifeOSDNA,
        context: PlanningContext
    ): Flow<FlightPlan> {
        return engine.generatePlan(tasks, dna, context)
            .onEach { plan ->
                repository.savePlan(plan)
            }
    }
}

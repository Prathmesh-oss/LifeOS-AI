package com.lifeosai.app.ai.planner.engine

import com.lifeosai.app.ai.planner.conflict.ConflictResolver
import com.lifeosai.app.ai.planner.estimator.DurationEstimator
import com.lifeosai.app.ai.planner.model.*
import com.lifeosai.app.ai.planner.prioritizer.TaskPrioritizer
import com.lifeosai.app.domain.model.LifeOSDNA
import com.lifeosai.app.domain.model.NexusTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The high-level Intelligence Orchestrator for Daily Planning.
 */
@Singleton
class PlannerEngine @Inject constructor(
    private val prioritizer: TaskPrioritizer,
    private val estimator: DurationEstimator,
    private val conflictResolver: ConflictResolver
) {

    /**
     * Generates an optimized Daily Flight Plan.
     */
    fun generatePlan(
        tasks: List<NexusTask>,
        dna: LifeOSDNA,
        context: PlanningContext
    ): Flow<FlightPlan> = flow {
        
        // 1. Prioritize Tasks
        val rankedTasks = prioritizer.rank(tasks, dna)
        
        // 2. Map Tasks to Time Blocks
        var currentTime = context.startOfDay
        val blocks = mutableListOf<ScheduledBlock>()
        
        rankedTasks.forEach { ranked ->
            val duration = estimator.estimate(ranked.task)
            val endTime = currentTime.plusMinutes(duration.toLong())
            
            if (endTime.isBefore(context.endOfDay)) {
                blocks.add(
                    ScheduledBlock(
                        id = UUID.randomUUID().toString(),
                        title = ranked.task.title,
                        type = if (duration >= 60) TimeBlockType.DEEP_WORK else TimeBlockType.SHALLOW_WORK,
                        startTime = currentTime,
                        endTime = endTime,
                        taskId = ranked.task.id,
                        priority = (ranked.score * 100).toInt()
                    )
                )
                currentTime = endTime.plusMinutes(15) // Automatic 15-min buffer/break
            }
        }

        // 3. Final flight plan construction
        val plan = FlightPlan(
            id = UUID.randomUUID().toString(),
            date = context.startOfDay,
            blocks = blocks,
            metadata = PlanMetadata(
                efficiencyScore = 0.85f,
                focusDensity = 0.7f,
                estimatedEnergyUsage = 0.6f,
                aiReasoning = "Scheduled ${blocks.size} tasks based on your ${dna.productivityType} profile."
            )
        )
        
        emit(plan)
    }
}

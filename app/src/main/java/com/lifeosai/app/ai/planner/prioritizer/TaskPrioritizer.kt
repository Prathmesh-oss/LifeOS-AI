package com.lifeosai.app.ai.planner.prioritizer

import com.lifeosai.app.domain.model.NexusTask
import com.lifeosai.app.domain.model.LifeOSDNA
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Autonomous Task Prioritizer.
 * Ranks tasks using a multi-factor weighting algorithm.
 */
@Singleton
class TaskPrioritizer @Inject constructor() {

    /**
     * Ranks tasks for the upcoming planning cycle.
     */
    fun rank(tasks: List<NexusTask>, dna: LifeOSDNA): List<RankedTask> {
        return tasks.map { task ->
            val score = calculateScore(task, dna)
            RankedTask(task, score)
        }.sortedByDescending { it.score }
    }

    private fun calculateScore(task: NexusTask, dna: LifeOSDNA): Float {
        var score = 0.5f

        // 1. Manual Priority Influence
        // NexusTask currently has simple Boolean completion, in production it has priority levels.
        // Assuming 1-5 scale for this logic.
        
        // 2. DNA Alignment
        if (dna.productivityType == "Deep Work Architect" && task.category == "Work") {
            score += 0.2f
        }

        // 3. Category Bias
        when (task.category) {
            "Work" -> score += 0.1f
            "Life" -> score += 0.05f
            "Health" -> score += 0.15f // Health is often high impact
        }

        return score.coerceIn(0.0f, 1.0f)
    }
}

data class RankedTask(
    val task: NexusTask,
    val score: Float
)

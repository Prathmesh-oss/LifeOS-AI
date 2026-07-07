package com.lifeosai.app.ai.planner.estimator

import com.lifeosai.app.domain.model.NexusTask
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AI-powered Duration Estimator.
 * Predicts task length based on historical patterns and task attributes.
 */
@Singleton
class DurationEstimator @Inject constructor() {

    private val defaultDuration = 30 // Minutes

    /**
     * Estimates the duration for a specific task.
     */
    fun estimate(task: NexusTask): Int {
        // In a full implementation, this looks at historical completion times
        // for similar tasks in the same category.
        
        return when (task.category) {
            "Work" -> 60
            "Health" -> 45
            "Life" -> 20
            else -> defaultDuration
        }
    }

    /**
     * Calculates the "Complexity Factor" for a task.
     */
    fun calculateComplexity(task: NexusTask): Float {
        // Longer titles or specific keywords often correlate with higher complexity.
        return if (task.title.length > 30) 1.5f else 1.0f
    }
}

package com.lifeosai.app.ai.dna.scorer

import com.lifeosai.app.ai.dna.model.DNAMetrics
import javax.inject.Inject
import javax.inject.Singleton

/**
 * High-level Scoring Engine for LifeOS DNA™.
 * Uses weighted algorithms to convert deep metrics into a human-readable Life Score.
 */
@Singleton
class DNAScorer @Inject constructor() {

    /**
     * Calculates the aggregate Life Score (0-100).
     */
    fun calculateAggregateLifeScore(metrics: DNAMetrics): Int {
        val productivityWeight = 0.25f
        val focusWeight = 0.25f
        val energyWeight = 0.15f
        val consistencyWeight = 0.20f
        val decisionWeight = 0.15f

        val rawScore = (metrics.productivityScore * productivityWeight) +
                (metrics.focusScore * focusWeight) +
                (metrics.energyScore * energyWeight) +
                (metrics.consistencyScore * consistencyWeight) +
                (metrics.decisionQualityScore * decisionWeight)

        // Penalty for high risk
        val riskPenalty = (metrics.burnoutRisk + metrics.stressRisk) * 10

        return (rawScore - riskPenalty).toInt().coerceIn(0, 100)
    }

    /**
     * Determines the User's Productivity Archetype based on metrics.
     */
    fun determineArchetype(metrics: DNAMetrics): String {
        return when {
            metrics.deepWorkScore > 85 && metrics.focusScore > 80 -> "Deep Work Architect"
            metrics.taskCompletionVelocity > 2.0f -> "Velocity Master"
            metrics.planningAccuracy > 0.9f -> "Strategic Planner"
            metrics.energyScore < 40 -> "Recovery Seeker"
            else -> "Steady Achiever"
        }
    }
}

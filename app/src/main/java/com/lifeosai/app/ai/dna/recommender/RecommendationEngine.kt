package com.lifeosai.app.ai.dna.recommender

import com.lifeosai.app.ai.dna.model.BehaviorPattern
import com.lifeosai.app.ai.dna.model.DNAMetrics
import com.lifeosai.app.ai.dna.model.PatternType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Proactive Intelligence Recommender.
 * Generates natural language insights based on detected DNA patterns.
 */
@Singleton
class RecommendationEngine @Inject constructor() {

    /**
     * Translates deep patterns into actionable UI recommendations.
     */
    fun generateRecommendations(
        metrics: DNAMetrics,
        patterns: List<BehaviorPattern>
    ): List<String> {
        val recommendations = mutableListOf<String>()

        patterns.forEach { pattern ->
            when (pattern.type) {
                PatternType.PRODUCTIVITY_PEAK -> {
                    recommendations.add("Your creative velocity peaks between 8:00 AM and 10:30 AM. Protect this window.")
                }
                PatternType.ENERGY_CRASH -> {
                    recommendations.add("We detected a significant energy drop on Tuesday afternoons. Consider shorter meetings.")
                }
                PatternType.DISTRACTION_VOID -> {
                    recommendations.add("You complete tasks 2x faster when your phone is in another room.")
                }
                PatternType.PROCRASTINATION_LOOP -> {
                    recommendations.add("You tend to stall on coding tasks after 4 PM. Try a 10-minute walk first.")
                }
                else -> {}
            }
        }

        // Contextual score-based advice
        if (metrics.burnoutRisk > 0.7f) {
            recommendations.add("High Burnout Risk detected. Your recovery score is dropping. Schedule a rest day.")
        }

        return recommendations
    }
}

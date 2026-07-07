package com.lifeosai.app.ai.dna.model

import java.time.LocalDateTime

/**
 * The intelligence foundation of LifeOS AI.
 * Captures deep behavioral metrics across multiple cognitive domains.
 */
data class DNAMetrics(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    // Core Scoring Vectors (0-100)
    val productivityScore: Int,
    val focusScore: Int,
    val energyScore: Int,
    val decisionQualityScore: Int,
    val deepWorkScore: Int,
    val consistencyScore: Int,
    val sleepImpactScore: Int,
    
    // Risk & Load Analysis
    val stressRisk: Float, // 0.0 - 1.0
    val burnoutRisk: Float, // 0.0 - 1.0
    val contextSwitchingRate: Float, // Switches per hour
    
    // Execution Intelligence
    val taskCompletionVelocity: Float, // Tasks per focus hour
    val planningAccuracy: Float, // Scheduled vs Completed ratio
    val calendarDiscipline: Float, // On-time start ratio
    val habitConsistency: Float, // Last 7 days streak weight
    
    // AI Metadata
    val aiConfidenceScore: Float
)

data class BehaviorPattern(
    val id: String,
    val label: String,
    val description: String,
    val type: PatternType,
    val impactScore: Float // Positive or Negative
)

enum class PatternType {
    PRODUCTIVITY_PEAK,
    DISTRACTION_VOID,
    PROCRASTINATION_LOOP,
    ENERGY_CRASH,
    FOCUS_FLOW
}

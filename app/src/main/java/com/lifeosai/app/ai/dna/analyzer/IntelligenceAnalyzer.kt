package com.lifeosai.app.ai.dna.analyzer

import com.lifeosai.app.ai.dna.model.BehaviorPattern
import com.lifeosai.app.ai.dna.model.DNAMetrics
import kotlinx.coroutines.flow.Flow

/**
 * Interface for specialized intelligence analyzers.
 * Each analyzer is responsible for a single domain of the user's LifeOS DNA.
 */
interface IntelligenceAnalyzer {
    val domain: AnalyzerDomain

    /**
     * Analyzes raw telemetry data to update DNA metrics.
     */
    suspend fun analyze(telemetry: Map<String, Any>): AnalysisResult
}

enum class AnalyzerDomain {
    FOCUS, ENERGY, PRODUCTIVITY, HABITS, SLEEP, EXECUTION
}

data class AnalysisResult(
    val domain: AnalyzerDomain,
    val partialMetrics: PartialDNAMetrics,
    val detectedPatterns: List<BehaviorPattern>
)

/**
 * A subset of DNAMetrics calculated by a specific analyzer.
 */
data class PartialDNAMetrics(
    val score: Int? = null,
    val risk: Float? = null,
    val velocity: Float? = null
)

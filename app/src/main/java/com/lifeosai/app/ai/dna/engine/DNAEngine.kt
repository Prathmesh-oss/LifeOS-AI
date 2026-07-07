package com.lifeosai.app.ai.dna.engine

import com.lifeosai.app.ai.dna.analyzer.IntelligenceAnalyzer
import com.lifeosai.app.ai.dna.model.DNAMetrics
import com.lifeosai.app.domain.model.LifeOSDNA
import kotlinx.coroutines.flow.StateFlow

/**
 * The Central Orchestrator for LifeOS DNA™.
 * Manages the pipeline: Telemetry -> Analysis -> Evolution -> State Update.
 */
interface DNAEngine {
    /**
     * The high-level UI representation of the DNA.
     */
    val currentDna: StateFlow<LifeOSDNA>

    /**
     * The raw, deep metrics used for internal AI reasoning.
     */
    val deepMetrics: StateFlow<DNAMetrics>

    /**
     * Entry point for new behavioral data.
     * Triggers the analysis of all registered [IntelligenceAnalyzer]s.
     */
    suspend fun ingestBehavior(telemetry: Map<String, Any>)

    /**
     * Forces an evolution cycle (usually weekly or daily).
     */
    suspend fun evolve()
}

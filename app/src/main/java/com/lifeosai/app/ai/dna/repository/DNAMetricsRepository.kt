package com.lifeosai.app.ai.dna.repository

import com.lifeosai.app.ai.dna.model.DNAMetrics
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for persisting and retrieving deep DNA metrics.
 */
interface DNAMetricsRepository {
    
    /**
     * Streams the latest calculated metrics.
     */
    fun observeLatestMetrics(): Flow<DNAMetrics?>

    /**
     * Persists a new snapshot of metrics.
     */
    suspend fun saveMetrics(metrics: DNAMetrics)

    /**
     * Retrieves historical metrics for trend analysis.
     */
    suspend fun getMetricsHistory(days: Int): List<DNAMetrics>
}

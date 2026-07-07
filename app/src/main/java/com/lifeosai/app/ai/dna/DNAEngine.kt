package com.lifeosai.app.ai.dna

import com.lifeosai.app.domain.model.LifeOSDNA
import kotlinx.coroutines.flow.Flow

interface DNAEngine {
    fun observeDNA(): Flow<LifeOSDNA>
    suspend fun evolve(activity: String)
}

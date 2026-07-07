package com.lifeosai.app.ai.dna

import com.lifeosai.app.domain.model.LifeOSDNA
import kotlinx.coroutines.flow.StateFlow

interface DNAEngine {
    /**
     * The current, evolving state of the user's digital personality.
     */
    val currentDna: StateFlow<LifeOSDNA>

    /**
     * Updates DNA traits based on new behavior telemetry.
     */
    suspend fun evolve(telemetry: Map<String, Any>)
}

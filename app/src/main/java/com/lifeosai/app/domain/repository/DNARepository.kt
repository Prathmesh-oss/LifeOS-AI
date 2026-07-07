package com.lifeosai.app.domain.repository

import com.lifeosai.app.domain.model.LifeOSDNA
import kotlinx.coroutines.flow.Flow

interface DNARepository {
    fun getDNA(): Flow<LifeOSDNA>
    suspend fun updateDNA(dna: LifeOSDNA)
}

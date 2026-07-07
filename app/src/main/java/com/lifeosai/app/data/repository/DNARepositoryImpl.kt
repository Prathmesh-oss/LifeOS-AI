package com.lifeosai.app.data.repository

import com.lifeosai.app.data.dao.DNADao
import com.lifeosai.app.data.entity.DNAEntity
import com.lifeosai.app.domain.model.LifeOSDNA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DNARepositoryImpl @Inject constructor(
    private val dnaDao: DNADao
) {
    fun getDNA(): Flow<LifeOSDNA?> {
        return dnaDao.getDNA().map { entity ->
            entity?.let {
                LifeOSDNA(
                    productivityType = it.productivityType,
                    lifeScore = it.lifeScore,
                    focusEnergy = it.focusEnergy,
                    aiStatus = it.aiStatus,
                    peakFocusStart = it.peakFocusStart,
                    peakFocusEnd = it.peakFocusEnd,
                    coachingStyle = it.coachingStyle
                )
            }
        }
    }

    suspend fun saveDNA(dna: LifeOSDNA) {
        val entity = DNAEntity(
            id = "default_dna", // Singleton DNA for now
            userId = "current_user",
            productivityType = dna.productivityType,
            lifeScore = dna.lifeScore,
            focusEnergy = dna.focusEnergy,
            aiStatus = dna.aiStatus,
            peakFocusStart = dna.peakFocusStart,
            peakFocusEnd = dna.peakFocusEnd,
            coachingStyle = dna.coachingStyle
        )
        dnaDao.insertDNA(entity)
    }
}

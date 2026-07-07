package com.lifeosai.app.domain.repository

import com.lifeosai.app.domain.model.LifeOSDNA
import com.lifeosai.app.domain.model.NexusData
import kotlinx.coroutines.flow.Flow

interface LifeOSRepository {
    fun getNexusData(): Flow<NexusData>
    fun getLifeOSDNA(): Flow<LifeOSDNA>
    suspend fun toggleTaskCompletion(taskId: String)
}

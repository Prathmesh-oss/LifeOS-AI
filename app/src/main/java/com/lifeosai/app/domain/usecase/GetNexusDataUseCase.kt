package com.lifeosai.app.domain.usecase

import com.lifeosai.app.domain.model.NexusData
import com.lifeosai.app.domain.repository.LifeOSRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNexusDataUseCase @Inject constructor(
    private val repository: LifeOSRepository
) {
    operator fun invoke(): Flow<NexusData> {
        return repository.getNexusData()
    }
}

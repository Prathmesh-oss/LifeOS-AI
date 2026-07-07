package com.lifeosai.app.domain.usecase.ai

import com.lifeosai.app.domain.model.Memory
import com.lifeosai.app.domain.repository.MemoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemoryTimelineUseCase @Inject constructor(
    private val repository: MemoryRepository
) {
    operator fun invoke(): Flow<List<Memory>> {
        return repository.observeAllMemories()
    }
}

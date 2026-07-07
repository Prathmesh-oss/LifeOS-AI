package com.lifeosai.app.domain.usecase

import com.lifeosai.app.domain.repository.LifeOSRepository
import javax.inject.Inject

class ToggleTaskCompletionUseCase @Inject constructor(
    private val repository: LifeOSRepository
) {
    suspend operator fun invoke(taskId: String) {
        repository.toggleTaskCompletion(taskId)
    }
}

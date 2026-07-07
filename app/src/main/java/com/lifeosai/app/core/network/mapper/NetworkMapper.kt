package com.lifeosai.app.core.network.mapper

import com.lifeosai.app.core.network.api.TaskDto
import com.lifeosai.app.domain.model.NexusTask

/**
 * Maps Network DTOs to Domain Models.
 * Ensures the networking layer's implementation details don't leak into the app.
 */
fun TaskDto.toDomain(): NexusTask {
    return NexusTask(
        id = id,
        title = title,
        isCompleted = false, // Default for new tasks from network
        category = "Inbox"   // Default category
    )
}

fun List<TaskDto>.toDomain(): List<NexusTask> = map { it.toDomain() }

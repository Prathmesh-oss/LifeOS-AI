package com.lifeosai.app.data.mapper

import com.lifeosai.app.data.entity.TaskEntity
import com.lifeosai.app.domain.model.NexusTask

fun TaskEntity.toDomain(): NexusTask {
    return NexusTask(
        id = id,
        title = title,
        isCompleted = isCompleted,
        category = category
    )
}

fun NexusTask.toEntity(projectId: String? = null, priority: Int = 0): TaskEntity {
    return TaskEntity(
        id = id,
        projectId = projectId,
        title = title,
        description = "",
        isCompleted = isCompleted,
        priority = priority,
        dueDate = null,
        category = category
    )
}

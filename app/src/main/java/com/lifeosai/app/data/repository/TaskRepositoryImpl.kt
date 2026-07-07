package com.lifeosai.app.data.repository

import com.lifeosai.app.data.dao.TaskDao
import com.lifeosai.app.data.mapper.toDomain
import com.lifeosai.app.data.mapper.toEntity
import com.lifeosai.app.domain.model.NexusTask
import com.lifeosai.app.domain.repository.LifeOSRepository // Re-using existing interface or can define new ones
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<NexusTask>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertTask(task: NexusTask) {
        taskDao.insertTask(task.toEntity())
    }

    suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        taskDao.toggleTaskCompletion(taskId, isCompleted)
    }

    suspend fun deleteTask(task: NexusTask) {
        taskDao.deleteTask(task.toEntity())
    }
}

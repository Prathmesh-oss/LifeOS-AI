package com.lifeosai.app.data.repository

import com.lifeosai.app.data.dao.NotificationDao
import com.lifeosai.app.data.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) {
    fun getAllNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotifications()
    }

    suspend fun sendNotification(title: String, body: String, type: String) {
        val notification = NotificationEntity(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            body = body,
            type = type
        )
        notificationDao.insertNotification(notification)
    }

    suspend fun markAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
    }

    suspend fun clearNotifications() {
        notificationDao.clearAllNotifications()
    }
}

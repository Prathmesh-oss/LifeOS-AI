package com.lifeosai.app.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lifeosai.app.data.dao.*
import com.lifeosai.app.data.entity.*

@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class,
        ProjectEntity::class,
        MemoryEntity::class,
        CalendarEntity::class,
        FocusSessionEntity::class,
        DNAEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class LifeOSDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    // abstract fun projectDao(): ProjectDao // Added later if needed or used TaskDao
    abstract fun memoryDao(): MemoryDao
    abstract fun calendarDao(): CalendarDao
    abstract fun dnaDao(): DNADao
    abstract fun notificationDao(): NotificationDao

    companion object {
        const val DATABASE_NAME = "lifeos_db"
    }
}

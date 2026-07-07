package com.lifeosai.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [
        Index(value = ["project_id"]),
        Index(value = ["due_date"]),
        Index(value = ["priority"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "project_id")
    val projectId: String?,
    val title: String,
    val description: String,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    val priority: Int,
    @ColumnInfo(name = "due_date")
    val dueDate: Long?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    val category: String,
    @ColumnInfo(name = "ai_importance_score")
    val aiImportanceScore: Float = 0f
)

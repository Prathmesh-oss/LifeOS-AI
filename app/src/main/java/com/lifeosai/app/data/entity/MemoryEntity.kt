package com.lifeosai.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "memories",
    indices = [Index(value = ["importance"])]
)
data class MemoryEntity(
    @PrimaryKey
    val id: String,
    val content: String,
    val importance: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "last_accessed_at")
    val lastAccessedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "vector_embedding")
    val vectorEmbedding: String? = null
)

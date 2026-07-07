package com.lifeosai.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "start_time")
    val startTime: Long,
    @ColumnInfo(name = "end_time")
    val endTime: Long,
    @ColumnInfo(name = "is_all_day")
    val isAllDay: Boolean = false,
    val location: String? = null,
    @ColumnInfo(name = "color")
    val color: Int? = null
)

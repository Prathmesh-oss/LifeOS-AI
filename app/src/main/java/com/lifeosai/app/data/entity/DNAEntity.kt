package com.lifeosai.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lifeos_dna")
data class DNAEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "productivity_type")
    val productivityType: String,
    @ColumnInfo(name = "life_score")
    val lifeScore: Int,
    @ColumnInfo(name = "focus_energy")
    val focusEnergy: String,
    @ColumnInfo(name = "ai_status")
    val aiStatus: String,
    @ColumnInfo(name = "peak_focus_start")
    val peakFocusStart: String,
    @ColumnInfo(name = "peak_focus_end")
    val peakFocusEnd: String,
    @ColumnInfo(name = "coaching_style")
    val coachingStyle: String,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)

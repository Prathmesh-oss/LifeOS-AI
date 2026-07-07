package com.lifeosai.app.domain.model

data class LifeOSDNA(
    val productivityType: String,
    val lifeScore: Int,
    val focusEnergy: String, // High, Medium, Low
    val aiStatus: String,
    val peakFocusStart: String,
    val peakFocusEnd: String,
    val coachingStyle: String
)

data class NexusTask(
    val id: String,
    val title: String,
    val isCompleted: Boolean,
    val category: String
)

data class NexusInsight(
    val text: String,
    val probability: Int? = null
)

data class NexusData(
    val userName: String,
    val dna: LifeOSDNA,
    val insight: NexusInsight,
    val primaryMission: String,
    val tasks: List<NexusTask>,
    val upcomingEvents: List<String>,
    val suggestions: List<String> = emptyList()
)

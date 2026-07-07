package com.lifeosai.app.domain.model

data class LifeOSDNA(
    val id: String,
    val productivityArchetype: String,
    val cognitiveLoadThreshold: Int,
    val peakFocusWindows: List<TimeRange>,
    val learningStyle: String,
    val evolvingTraits: Map<String, Float>
)

data class TimeRange(
    val startHour: Int,
    val endHour: Int
)

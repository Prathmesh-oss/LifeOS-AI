package com.lifeosai.app.core.dna

import com.lifeosai.app.core.intelligence.ApplicationScope
import com.lifeosai.app.domain.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LIFEOS DNA™ ENGINE - THE DIGITAL TWIN
 * 
 * Architecture:
 * 1. DNA Profile: Multi-dimensional immutable model of user behavior.
 * 2. DNA Score: Quantitative metrics with trends and confidence.
 * 3. Learning Engine: Background processor that evolves the DNA from life events.
 * 4. DNA Repository: Reactive interface for persistence and observation.
 */

// --- 1. DNA PROFILE MODELS ---

data class UserDNAProfile(
    val productivityStyle: ProductivityStyle = ProductivityStyle.UNKNOWN,
    val focusPattern: FocusPattern = FocusPattern(),
    val energyCurve: Map<Int, Float> = emptyMap(), // Hour (0-23) to Energy Level (0.0-1.0)
    val sleepPreference: SleepPreference = SleepPreference.UNKNOWN,
    val learningStyle: LearningStyle = LearningStyle.MIXED,
    val workPreferences: WorkPreferences = WorkPreferences(),
    val confidenceScore: Float = 0.0f,
    val lastEvolved: LocalDateTime = LocalDateTime.now()
)

enum class ProductivityStyle { UNKNOWN, SPRINTER, MARATHONER, DEEP_WORKER, MULTI_TASKER }
enum class SleepPreference { UNKNOWN, MORNING_LARK, NIGHT_OWL, BIPHASIC }
enum class LearningStyle { VISUAL, AUDITORY, TEXTUAL, KINESTHETIC, MIXED }

data class FocusPattern(
    val averageFocusDuration: Int = 0, // Minutes
    val peakFocusStart: LocalTime = LocalTime.of(9, 0),
    val distractionSensitivity: Float = 0.5f // 0.0 to 1.0
)

data class WorkPreferences(
    val preferredBreakDuration: Int = 15,
    val preferredMeetingWindow: Pair<LocalTime, LocalTime> = LocalTime.of(13, 0) to LocalTime.of(16, 0),
    val notificationSensitivity: Float = 0.7f
)

// --- 2. DNA SCORING ---

data class DNAScore(
    val currentValue: Float,
    val trend: ScoreTrend,
    val history: List<HistoricalValue>,
    val lastUpdated: LocalDateTime = LocalDateTime.now(),
    val confidence: Float
)

enum class ScoreTrend { RISING, STABLE, FALLING }
data class HistoricalValue(val timestamp: LocalDateTime, val value: Float)

data class LifeOSDNAMetrics(
    val focusScore: DNAScore,
    val consistencyScore: DNAScore,
    val planningScore: DNAScore,
    val executionScore: DNAScore,
    val wellbeingScore: DNAScore
)

// --- 3. REPOSITORY INTERFACE ---

interface DNARepository {
    fun getDNAProfile(): Flow<UserDNAProfile>
    fun getDNAMetrics(): Flow<LifeOSDNAMetrics>
    suspend fun updateDNA(profile: UserDNAProfile)
    suspend fun recordEvent(eventType: String, metadata: Map<String, String>)
    suspend fun resetDNA()
}

// --- 4. DNA EVOLUTION ENGINE ---

@Singleton
class LifeOSDNAEngine @Inject constructor(
    private val repository: DNARepository,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val _dnaState = MutableStateFlow(UserDNAProfile())
    val dnaState: StateFlow<UserDNAProfile> = _dnaState.asStateFlow()

    private val _metricsState = MutableStateFlow<LifeOSDNAMetrics?>(null)
    val metricsState: StateFlow<LifeOSDNAMetrics?> = _metricsState.asStateFlow()

    init {
        observeDNABase()
    }

    private fun observeDNABase() {
        repository.getDNAProfile()
            .onEach { _dnaState.value = it }
            .launchIn(scope)

        repository.getDNAMetrics()
            .onEach { _metricsState.value = it }
            .launchIn(scope)
    }

    /**
     * The core evolution logic. This function is triggered by the Intelligence Core 
     * whenever a significant LifeEvent occurs (e.g., TaskCompleted, FocusSessionFinished).
     */
    fun evolve(event: String, metadata: Map<String, String>) {
        scope.launch(Dispatchers.Default) {
            val currentDNA = _dnaState.value
            
            // ANALYZE PATTERN
            val evolvedDNA = when (event) {
                "TASK_COMPLETED" -> analyzeTaskCompletion(currentDNA, metadata)
                "FOCUS_SESSION_FINISHED" -> analyzeFocusSession(currentDNA, metadata)
                "REMINDER_SKIPPED" -> analyzeAvoidanceBehavior(currentDNA, metadata)
                else -> currentDNA
            }

            if (evolvedDNA != currentDNA) {
                repository.updateDNA(evolvedDNA.copy(lastEvolved = LocalDateTime.now()))
            }
            
            repository.recordEvent(event, metadata)
        }
    }

    private fun analyzeTaskCompletion(dna: UserDNAProfile, data: Map<String, String>): UserDNAProfile {
        val completionTime = LocalDateTime.parse(data["timestamp"] ?: LocalDateTime.now().toString())
        val hour = completionTime.hour
        
        // Update Energy Curve based on when tasks are actually getting done
        val updatedCurve = dna.energyCurve.toMutableMap()
        val currentEnergy = updatedCurve.getOrDefault(hour, 0.5f)
        updatedCurve[hour] = (currentEnergy + 0.1f).coerceAtMost(1.0f)
        
        return dna.copy(energyCurve = updatedCurve)
    }

    private fun analyzeFocusSession(dna: UserDNAProfile, data: Map<String, String>): UserDNAProfile {
        val duration = data["duration_minutes"]?.toIntOrNull() ?: 0
        val currentFocus = dna.focusPattern
        
        val newAverage = if (currentFocus.averageFocusDuration == 0) duration 
                         else (currentFocus.averageFocusDuration + duration) / 2
                         
        return dna.copy(
            focusPattern = currentFocus.copy(averageFocusDuration = newAverage),
            productivityStyle = if (newAverage > 45) ProductivityStyle.DEEP_WORKER else dna.productivityStyle
        )
    }

    private fun analyzeAvoidanceBehavior(dna: UserDNAProfile, data: Map<String, String>): UserDNAProfile {
        // High notification sensitivity if user constantly skips reminders
        return dna.copy(
            workPreferences = dna.workPreferences.copy(
                notificationSensitivity = (dna.workPreferences.notificationSensitivity + 0.05f).coerceAtMost(1.0f)
            )
        )
    }

    // AI Helper: Personalize a generic intent using the DNA
    fun personalize(intent: AIIntent): AIIntent {
        val dna = _dnaState.value
        return when (intent) {
            is AIIntent.PlanRequest -> {
                // If the user is a Deep Worker, prioritize longer blocks in the plan
                intent.copy(timeframe = intent.timeframe) // Actual logic would adjust schedule parameters
            }
            is AIIntent.CreateTask -> {
                // Adjust priority based on energy curve for that deadline
                intent
            }
            else -> intent
        }
    }
}

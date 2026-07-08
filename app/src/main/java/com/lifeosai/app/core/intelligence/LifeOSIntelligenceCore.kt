package com.lifeosai.app.core.intelligence

import com.lifeosai.app.domain.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LIFEOS INTELLIGENCE CORE - THE DIGITAL BRAIN
 * 
 * Architecture:
 * 1. Event Bus: Decoupled reactive communication.
 * 2. Context Engine: Real-time state tracking.
 * 3. Modular Engines: Intent, Memory, Reasoning, Prediction, Recommendation.
 * 4. Orchestrator: Central coordination and State Management.
 */

// --- 1. EVENT SYSTEM ---

sealed class LifeOSIntelligenceEvent {
    data class TaskCompleted(val taskId: String) : LifeOSIntelligenceEvent()
    data class VoiceCaptured(val transcript: String) : LifeOSIntelligenceEvent()
    data class ScreenChanged(val screenName: String) : LifeOSIntelligenceEvent()
    data class UserMoodChanged(val mood: String) : LifeOSIntelligenceEvent()
    data class EnergyLevelUpdated(val level: Int) : LifeOSIntelligenceEvent()
    data class MemoryStored(val memory: AIMemory) : LifeOSIntelligenceEvent()
    object SessionStarted : LifeOSIntelligenceEvent()
}

@Singleton
class IntelligenceEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<LifeOSIntelligenceEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    suspend fun emit(event: LifeOSIntelligenceEvent) {
        _events.emit(event)
    }
}

// --- 2. STATE DEFINITIONS ---

data class LifeOSAIState(
    val context: UserContext = UserContext(),
    val predictions: List<AIPrediction> = emptyList(),
    val recommendations: List<AIRecommendation> = emptyList(),
    val status: AIStatus = AIStatus.IDLE,
    val reasoningLog: String = "",
    val confidenceScore: Float = 1.0f
)

data class AIPrediction(
    val type: String,
    val description: String,
    val probability: Float,
    val explanation: String
)

data class AIRecommendation(
    val title: String,
    val content: String,
    val priority: Priority,
    val actionIntent: AIIntent? = null
)

enum class AIStatus { IDLE, THINKING, LEARNING, ACTING }

// --- 3. ENGINE INTERFACES ---

interface IntentEngine {
    suspend fun classify(input: String, context: UserContext): AIIntent
}

interface MemoryEngine {
    suspend fun retrieveRelevant(context: UserContext): List<AIMemory>
    suspend fun store(interaction: String, intent: AIIntent)
}

interface ReasoningEngine {
    suspend fun reason(intent: AIIntent, context: UserContext, memories: List<AIMemory>): AIResponse
}

interface PredictionEngine {
    suspend fun runInference(context: UserContext, history: List<AIMemory>): List<AIPrediction>
}

interface RecommendationEngine {
    suspend fun generate(context: UserContext, predictions: List<AIPrediction>): List<AIRecommendation>
}

// --- 4. CORE ORCHESTRATOR ---

@Singleton
class LifeOSIntelligenceCore @Inject constructor(
    private val eventBus: IntelligenceEventBus,
    private val intentEngine: IntentEngine,
    private val memoryEngine: MemoryEngine,
    private val reasoningEngine: ReasoningEngine,
    private val predictionEngine: PredictionEngine,
    private val recommendationEngine: RecommendationEngine,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(LifeOSAIState())
    val state: StateFlow<LifeOSAIState> = _state.asStateFlow()

    init {
        observeEvents()
        startBackgroundLearning()
    }

    private fun observeEvents() {
        scope.launch {
            eventBus.events.collect { event ->
                handleEvent(event)
            }
        }
    }

    private suspend fun handleEvent(event: LifeOSIntelligenceEvent) {
        val currentContext = _state.value.context
        
        val updatedContext = when (event) {
            is LifeOSIntelligenceEvent.ScreenChanged -> currentContext.copy(activeScreen = event.screenName)
            is LifeOSIntelligenceEvent.EnergyLevelUpdated -> currentContext.copy(energyLevel = event.level)
            is LifeOSIntelligenceEvent.VoiceCaptured -> {
                processUserVoice(event.transcript, currentContext)
                currentContext
            }
            else -> currentContext
        }

        _state.update { it.copy(context = updatedContext) }
        triggerReasoningCycle()
    }

    private suspend fun processUserVoice(text: String, context: UserContext) {
        _state.update { it.copy(status = AIStatus.THINKING) }
        
        val intent = intentEngine.classify(text, context)
        val relevantMemories = memoryEngine.retrieveRelevant(context)
        val response = reasoningEngine.reason(intent, context, relevantMemories)
        
        // Log reasoning and update state
        _state.update { 
            it.copy(
                status = AIStatus.IDLE,
                reasoningLog = "Processed intent: ${intent::class.simpleName}"
            ) 
        }
        
        memoryEngine.store(text, intent)
    }

    private fun triggerReasoningCycle() {
        scope.launch {
            val context = _state.value.context
            val memories = memoryEngine.retrieveRelevant(context)
            
            val predictions = predictionEngine.runInference(context, memories)
            val recommendations = recommendationEngine.generate(context, predictions)
            
            _state.update { 
                it.copy(
                    predictions = predictions,
                    recommendations = recommendations
                ) 
            }
        }
    }

    private fun startBackgroundLearning() {
        scope.launch {
            while (isActive) {
                delay(300_000) // 5 minutes
                _state.update { it.copy(status = AIStatus.LEARNING) }
                triggerReasoningCycle()
                _state.update { it.copy(status = AIStatus.IDLE) }
            }
        }
    }

    // Public API for ViewModels
    fun submitEvent(event: LifeOSIntelligenceEvent) {
        scope.launch { eventBus.emit(event) }
    }
}

// Qualifier for Hilt
@Retention(AnnotationRetention.BINARY)
@javax.inject.Qualifier
annotation class ApplicationScope

package com.lifeosai.app.core.automation

import com.lifeosai.app.core.intelligence.ApplicationScope
import com.lifeosai.app.domain.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LIFEOS SKILLS & AUTOMATION ENGINE™
 * 
 * Architecture:
 * 1. Declarative Workflow Models: Trigger -> Conditions -> Actions.
 * 2. Reactive Execution Engine: Monitors life events and dispatches workflows.
 * 3. Atomic Actions: Discrete operations that interact with the wider system.
 * 4. Persistence & Scheduling: Integration with WorkManager and Room for durability.
 */

// --- 1. WORKFLOW DOMAIN MODELS ---

sealed class AutomationTrigger {
    data class Time(val cronExpression: String) : AutomationTrigger()
    data class TaskStateChanged(val status: String) : AutomationTrigger()
    data class GoalReached(val goalId: String) : AutomationTrigger()
    data class FocusSessionEnded(val durationMinutes: Int) : AutomationTrigger()
    object BatteryLow : AutomationTrigger()
    data class Custom(val eventType: String) : AutomationTrigger()
}

sealed class AutomationCondition {
    data class EnergyAbove(val level: Int) : AutomationCondition()
    data class IsDay(val dayOfWeek: Int) : AutomationCondition()
    data class IsFocusMode(val enabled: Boolean) : AutomationCondition()
    data class DNAConfidenceAbove(val threshold: Float) : AutomationCondition()
}

sealed class AutomationAction {
    data class CreateTask(val title: String, val priority: Priority) : AutomationAction()
    data class Notify(val message: String) : AutomationAction()
    data class UpdateDNA(val trait: String, val value: String) : AutomationAction()
    data class RunAgent(val agentId: String, val command: String) : AutomationAction()
    data class StartFocusSession(val durationMinutes: Int) : AutomationAction()
}

data class AutomationWorkflow(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val trigger: AutomationTrigger,
    val conditions: List<AutomationCondition> = emptyList(),
    val actions: List<AutomationAction>,
    val priority: Int = 0,
    val isEnabled: Boolean = true,
    val lastExecuted: LocalDateTime? = null
)

// --- 2. REPOSITORY CONTRACT ---

interface AutomationRepository {
    fun observeWorkflows(): Flow<List<AutomationWorkflow>>
    suspend fun saveWorkflow(workflow: AutomationWorkflow)
    suspend fun deleteWorkflow(id: String)
    suspend fun logExecution(workflowId: String, status: String, error: String? = null)
}

// --- 3. EXECUTION ENGINE ---

@Singleton
class LifeOSAutomationEngine @Inject constructor(
    private val repository: AutomationRepository,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val _workflows = MutableStateFlow<List<AutomationWorkflow>>(emptyList())
    val workflows: StateFlow<List<AutomationWorkflow>> = _workflows.asStateFlow()

    init {
        scope.launch {
            repository.observeWorkflows().collect { _workflows.value = it }
        }
    }

    /**
     * Entry point for external events to trigger automations.
     */
    fun onEvent(trigger: AutomationTrigger) {
        scope.launch(Dispatchers.Default) {
            _workflows.value
                .filter { it.isEnabled && it.trigger::class == trigger::class }
                .forEach { workflow ->
                    if (evaluateConditions(workflow.conditions)) {
                        executeWorkflow(workflow)
                    }
                }
        }
    }

    private suspend fun evaluateConditions(conditions: List<AutomationCondition>): Boolean {
        // In a production app, this would query the DNA Engine, Context Engine, and Repositories
        return conditions.all { condition ->
            when (condition) {
                is AutomationCondition.EnergyAbove -> true // Placeholder for ContextEngine.energy
                is AutomationCondition.IsDay -> true // Placeholder for current time check
                else -> true
            }
        }
    }

    private suspend fun executeWorkflow(workflow: AutomationWorkflow) {
        try {
            workflow.actions.forEach { action ->
                performAction(action)
            }
            repository.logExecution(workflow.id, "SUCCESS")
            repository.saveWorkflow(workflow.copy(lastExecuted = LocalDateTime.now()))
        } catch (e: Exception) {
            repository.logExecution(workflow.id, "FAILED", e.message)
        }
    }

    private suspend fun performAction(action: AutomationAction) {
        when (action) {
            is AutomationAction.CreateTask -> {
                // Interacts with TaskRepository
            }
            is AutomationAction.Notify -> {
                // Interacts with NotificationManager
            }
            is AutomationAction.RunAgent -> {
                // Interacts with AgentOrchestrator
            }
            else -> { /* Log unsupported action */ }
        }
    }

    /**
     * AI-Driven Suggestion: Analyzes patterns to recommend new automations.
     */
    fun suggestAutomation(behaviorPattern: String): AutomationWorkflow? {
        // Logic to transform patterns (from DNA Engine) into potential workflows
        return null 
    }
}

package com.lifeosai.app.core.memory

import com.lifeosai.app.core.intelligence.ApplicationScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LIFEOS NEURAL MEMORY GRAPH™ - CORE KNOWLEDGE SYSTEM
 * 
 * Architecture:
 * 1. Semantic Property Graph: Nodes represent entities; Edges represent typed relationships.
 * 2. Multi-Factor Ranking: Combines Recency, Importance, and Connectivity.
 * 3. Neighborhood Context: Retrieves connected clusters for RAG (Retrieval-Augmented Generation).
 * 4. Memory Consolidation & Aging: Intelligent cleanup and significance tracking.
 */

// --- 1. DOMAIN MODELS ---

enum class MemoryNodeType {
    TASK, GOAL, PROJECT, NOTE, JOURNAL_ENTRY, CALENDAR_EVENT, 
    REMINDER, HABIT, PERSON, PLACE, TOPIC, MEETING, 
    VOICE_MEMO, BRAIN_CAPTURE, AI_INSIGHT, AI_RECOMMENDATION, 
    FOCUS_SESSION, LEARNING_RESOURCE, DOCUMENT, LINK
}

enum class RelationshipType {
    BELONGS_TO, HAS_TASK, INVOLVES, REFERENCES, RELATED_TO, 
    SUPPORTS, CREATES, REFLECTS, GENERATED_FROM, PART_OF
}

data class MemoryNode(
    val id: String = UUID.randomUUID().toString(),
    val type: MemoryNodeType,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val importanceScore: Float = 0.5f,
    val confidenceScore: Float = 1.0f,
    val source: String = "USER",
    val tags: Set<String> = emptySet(),
    val metadata: Map<String, String> = emptyMap(),
    val embedding: List<Float>? = null
)

data class MemoryRelationship(
    val id: String = UUID.randomUUID().toString(),
    val fromNodeId: String,
    val toNodeId: String,
    val type: RelationshipType,
    val strength: Float = 1.0f,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class MemoryContext(
    val mainNode: MemoryNode,
    val relatedNodes: List<MemoryNode>,
    val relationships: List<MemoryRelationship>
)

// --- 2. REPOSITORY CONTRACT ---

interface MemoryGraphRepository {
    fun observeNodes(): Flow<List<MemoryNode>>
    fun observeRelationships(): Flow<List<MemoryRelationship>>
    suspend fun upsertNode(node: MemoryNode)
    suspend fun deleteNode(nodeId: String)
    suspend fun connect(from: String, to: String, type: RelationshipType, strength: Float = 1.0f)
    suspend fun getNodesByIds(ids: List<String>): List<MemoryNode>
    suspend fun findNodesByType(type: MemoryNodeType): List<MemoryNode>
    suspend fun searchNodes(query: String): List<MemoryNode>
}

// --- 3. GRAPH ENGINE ---

@Singleton
class LifeOSNeuralMemoryGraph @Inject constructor(
    private val repository: MemoryGraphRepository,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val _nodes = MutableStateFlow<List<MemoryNode>>(emptyList())
    private val _edges = MutableStateFlow<List<MemoryRelationship>>(emptyList())

    init {
        scope.launch {
            repository.observeNodes().collect { _nodes.value = it }
        }
        scope.launch {
            repository.observeRelationships().collect { _edges.value = it }
        }
    }

    /**
     * Retrieves a node and its semantic neighborhood.
     * This provides the "Context Window" for AI reasoning.
     */
    suspend fun getExpandedContext(nodeId: String, depth: Int = 1): MemoryContext? {
        val mainNode = _nodes.value.find { it.id == nodeId } ?: return null
        
        // Find connected nodes within the specified depth
        val relatedEdgeIds = mutableSetOf<String>()
        val relatedNodeIds = mutableSetOf<String>()
        
        var currentLevelNodes = setOf(nodeId)
        repeat(depth) {
            val nextLevelNodes = mutableSetOf<String>()
            _edges.value.filter { it.fromNodeId in currentLevelNodes || it.toNodeId in currentLevelNodes }.forEach { edge ->
                relatedEdgeIds.add(edge.id)
                val neighborId = if (edge.fromNodeId in currentLevelNodes) edge.toNodeId else edge.fromNodeId
                if (neighborId != nodeId) {
                    nextLevelNodes.add(neighborId)
                    relatedNodeIds.add(neighborId)
                }
            }
            currentLevelNodes = nextLevelNodes
        }

        val relatedNodes = _nodes.value.filter { it.id in relatedNodeIds }
        val relationships = _edges.value.filter { it.id in relatedEdgeIds }

        return MemoryContext(mainNode, relatedNodes, relationships)
    }

    /**
     * Ranks memories based on the Neural Significance Algorithm.
     * Score = (Importance * RecencyWeight) + (Connectivity * 0.2)
     */
    fun rankMemories(nodes: List<MemoryNode>): List<MemoryNode> {
        val now = LocalDateTime.now()
        return nodes.sortedByDescending { node ->
            val daysOld = ChronoUnit.DAYS.between(node.updatedAt, now).coerceAtLeast(0)
            val recencyWeight = (1.0f / (daysOld + 1).toDouble()).toFloat()
            
            val connectivity = _edges.value.count { it.fromNodeId == node.id || it.toNodeId == node.id }
            
            (node.importanceScore * recencyWeight) + (connectivity * 0.1f)
        }
    }

    /**
     * Memory Consolidation Logic: Detects potential duplicates or highly related ideas.
     */
    suspend fun findMergeSuggestions(): List<Pair<MemoryNode, MemoryNode>> {
        val suggestions = mutableListOf<Pair<MemoryNode, MemoryNode>>()
        val nodes = _nodes.value
        
        for (i in nodes.indices) {
            for (j in i + 1 until nodes.size) {
                if (isSemanticMatch(nodes[i], nodes[j])) {
                    suggestions.add(nodes[i] to nodes[j])
                }
            }
        }
        return suggestions
    }

    private fun isSemanticMatch(nodeA: MemoryNode, nodeB: MemoryNode): Boolean {
        if (nodeA.type != nodeB.type) return false
        
        // Basic title similarity check (placeholder for actual embedding distance)
        val titleA = nodeA.title.lowercase()
        val titleB = nodeB.title.lowercase()
        return titleA == titleB || titleA.contains(titleB) || titleB.contains(titleA)
    }

    /**
     * Aging Strategy: Gradually reduces importance of stagnant memories.
     */
    suspend fun applyAging() {
        val now = LocalDateTime.now()
        _nodes.value.forEach { node ->
            val monthsOld = ChronoUnit.MONTHS.between(node.updatedAt, now)
            if (monthsOld > 3) {
                val decayedScore = (node.importanceScore * 0.9f).coerceAtLeast(0.1f)
                if (decayedScore != node.importanceScore) {
                    repository.upsertNode(node.copy(importanceScore = decayedScore))
                }
            }
        }
    }
}

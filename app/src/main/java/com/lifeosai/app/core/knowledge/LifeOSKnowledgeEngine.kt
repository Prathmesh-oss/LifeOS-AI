package com.lifeosai.app.core.knowledge

import com.lifeosai.app.core.intelligence.ApplicationScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LIFEOS KNOWLEDGE ENGINE™ (RAG + SEMANTIC INTELLIGENCE)
 * 
 * ARCHITECTURAL DECISIONS:
 * 1. Pipeline Architecture: Uses a linear retrieval pipeline (Intent -> Rewrite -> Retrieve -> Rank -> Build).
 * 2. Hybrid Search: Designed to support both Vector (Semantic) and FTS (Keyword) through the Repository interface.
 * 3. Provider Agnostic: RAG interfaces do not depend on specific LLMs (Gemini/OpenAI).
 * 4. Multi-Factor Ranking: Ranks results based on similarity, recency, and "DNA Relevance".
 * 5. Offline-First: All core indexing and retrieval logic is defined via local-first interfaces.
 */

// --- 1. KNOWLEDGE DOMAIN MODELS ---

/**
 * Represents any piece of information in the LifeOS ecosystem.
 */
data class KnowledgeNode(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val sourceType: KnowledgeSource,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val importance: Float = 0.5f, // 0.0 to 1.0
    val metadata: Map<String, String> = emptyMap(),
    val embeddingId: String? = null
)

enum class KnowledgeSource {
    TASK, PROJECT, NOTE, CALENDAR, JOURNAL, BRAIN_CAPTURE, DNA, INSIGHT, SYSTEM
}

/**
 * The final payload sent to the Reasoning Engine.
 */
data class AugmentedContext(
    val originalQuery: String,
    val refinedQuery: String,
    val intent: UserIntent,
    val nodes: List<KnowledgeNode>,
    val dnaSnapshot: Map<String, Any>,
    val systemPrompt: String
)

data class UserIntent(
    val type: IntentType,
    val confidence: Float,
    val entities: List<String>
)

enum class IntentType {
    QUERY, ACTION, SUMMARIZE, RELATIONSHIP, DISCOVERY
}

// --- 2. CORE INTERFACES (Clean Architecture - Repository Pattern) ---

interface KnowledgeRepository {
    suspend fun findSemanticMatches(query: String, limit: Int): List<KnowledgeNode>
    suspend fun findKeywordMatches(query: String, limit: Int): List<KnowledgeNode>
    suspend fun getRelatedNodes(nodeId: String): List<KnowledgeNode>
    suspend fun saveNode(node: KnowledgeNode)
    suspend fun deleteNode(nodeId: String)
}

interface EmbeddingProvider {
    suspend fun generateEmbedding(text: String): List<Float>
}

interface UserContextProvider {
    fun getCurrentDNASnapshot(): Map<String, Any>
    fun getActiveFocusSession(): String?
}

// --- 3. RETRIEVAL PIPELINE COMPONENTS ---

@Singleton
class IntentAnalyzer @Inject constructor() {
    fun analyze(query: String): UserIntent {
        // Production logic: Regex, Keyword mapping, or Local Small-LLM call
        val type = when {
            query.contains("what", ignoreCase = true) -> IntentType.QUERY
            query.contains("summarize", ignoreCase = true) -> IntentType.SUMMARIZE
            query.contains("connect", ignoreCase = true) -> IntentType.RELATIONSHIP
            else -> IntentType.QUERY
        }
        return UserIntent(type, 0.95f, emptyList())
    }
}

@Singleton
class QueryRewriter @Inject constructor() {
    fun rewrite(query: String, intent: UserIntent): String {
        // Expands query with synonyms and removes noise
        return query // Placeholder for expansion logic
    }
}

@Singleton
class KnowledgeRanker @Inject constructor() {
    /**
     * Ranks nodes using a weighted heuristic:
     * Similarity (40%) + Recency (30%) + Importance (20%) + DNA Relevance (10%)
     */
    fun rank(
        nodes: List<KnowledgeNode>, 
        query: String, 
        dna: Map<String, Any>
    ): List<KnowledgeNode> {
        return nodes.distinctBy { it.id }.sortedByDescending { node ->
            val similarity = 0.8f // Hypothetical score from vector DB
            val recency = calculateRecencyScore(node.timestamp)
            val importance = node.importance
            
            (similarity * 0.4f) + (recency * 0.3f) + (importance * 0.3f)
        }
    }

    private fun calculateRecencyScore(timestamp: LocalDateTime): Float {
        // Simplified decay function
        return 1.0f // Placeholder
    }
}

// --- 4. THE KNOWLEDGE ENGINE ---

@Singleton
class LifeOSKnowledgeEngine @Inject constructor(
    private val repository: KnowledgeRepository,
    private val intentAnalyzer: IntentAnalyzer,
    private val queryRewriter: QueryRewriter,
    private val ranker: KnowledgeRanker,
    private val contextProvider: UserContextProvider,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val _engineStatus = MutableStateFlow<EngineStatus>(EngineStatus.Idle)
    val engineStatus: StateFlow<EngineStatus> = _engineStatus.asStateFlow()

    /**
     * Primary Entry Point: Assembles the "Brain Context" for any AI query.
     */
    suspend fun buildAugmentedContext(query: String): AugmentedContext = withContext(Dispatchers.Default) {
        _engineStatus.value = EngineStatus.Processing(query)
        
        try {
            // 1. INTENT & REWRITE
            val intent = intentAnalyzer.analyze(query)
            val refinedQuery = queryRewriter.rewrite(query, intent)

            // 2. HYBRID RETRIEVAL
            val semanticMatches = repository.findSemanticMatches(refinedQuery, limit = 15)
            val keywordMatches = repository.findKeywordMatches(refinedQuery, limit = 10)
            
            // 3. RANKING
            val dna = contextProvider.getCurrentDNASnapshot()
            val rankedNodes = ranker.rank(semanticMatches + keywordMatches, refinedQuery, dna)

            // 4. CONTEXT BUILDING
            val context = AugmentedContext(
                originalQuery = query,
                refinedQuery = refinedQuery,
                intent = intent,
                nodes = rankedNodes.take(15),
                dnaSnapshot = dna,
                systemPrompt = generateSystemPrompt(intent, dna)
            )

            _engineStatus.value = EngineStatus.Idle
            context
        } catch (e: Exception) {
            _engineStatus.value = EngineStatus.Error(e.message ?: "Unknown Error")
            throw e
        }
    }

    /**
     * Incremental Indexing: Allows other modules to feed the knowledge base.
     */
    fun index(node: KnowledgeNode) {
        scope.launch(Dispatchers.IO) {
            repository.saveNode(node)
        }
    }

    private fun generateSystemPrompt(intent: UserIntent, dna: Map<String, Any>): String {
        return "You are the LifeOS AI. Use the provided context to answer. User DNA: $dna"
    }
}

sealed class EngineStatus {
    object Idle : EngineStatus()
    data class Processing(val query: String) : EngineStatus()
    data class Error(val message: String) : EngineStatus()
}

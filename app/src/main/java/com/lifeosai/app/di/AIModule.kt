package com.lifeosai.app.di

import com.lifeosai.app.ai.chat.ChatEngine
import com.lifeosai.app.ai.dna.DNAEngine
import com.lifeosai.app.ai.memory.MemoryEngine
import com.lifeosai.app.ai.planner.PlannerEngine
import com.lifeosai.app.ai.reasoning.ReasoningEngine
import com.lifeosai.app.ai.scheduler.SchedulerEngine
import com.lifeosai.app.ai.summarizer.SummarizerEngine
import com.lifeosai.app.ai.voice.VoiceEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for AI Engines.
 * In a real production app, these would be bound to specific implementations (e.g., Gemini, TFLite, etc.)
 */
@Module
@InstallIn(SingletonComponent::class)
object AIModule {

    // Placeholder for actual AI implementations
    // These would typically be provided by a Data module that implements these interfaces.
    
    // For now, we omit implementations to keep the architecture clean as requested.
}

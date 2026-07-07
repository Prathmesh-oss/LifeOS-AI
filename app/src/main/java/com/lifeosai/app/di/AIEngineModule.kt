package com.lifeosai.app.di

import com.lifeosai.app.ai.dna.DNAEngine
import com.lifeosai.app.ai.reasoning.ReasoningEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton
import com.lifeosai.app.domain.model.LifeOSDNA

@Module
@InstallIn(SingletonComponent::class)
object AIEngineModule {

    @Provides
    @Singleton
    fun provideReasoningEngine(): ReasoningEngine {
        return object : ReasoningEngine {
            override fun reason(prompt: String): Flow<String> = flow {
                emit("AI response to: $prompt")
            }
        }
    }

    @Provides
    @Singleton
    fun provideDNAEngine(): DNAEngine {
        return object : DNAEngine {
            override fun observeDNA(): Flow<LifeOSDNA> = flow {
                // Mock DNA
            }
            override suspend fun evolve(activity: String) {
                // Mock evolve
            }
        }
    }
}

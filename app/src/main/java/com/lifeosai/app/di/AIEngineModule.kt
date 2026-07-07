package com.lifeosai.app.di

import com.lifeosai.app.ai.reasoning.ReasoningEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow

@Module
@InstallIn(SingletonComponent::class)
object AIEngineModule {

    @Provides
    @Singleton
    fun provideReasoningEngine(): ReasoningEngine {
        return object : ReasoningEngine {
            override fun infer(context: com.lifeosai.app.ai.reasoning.AIContext) = flow {
                emit(com.lifeosai.app.ai.reasoning.InferenceResult("Analyzing...", emptyList(), 1.0f))
            }
        }
    }
}

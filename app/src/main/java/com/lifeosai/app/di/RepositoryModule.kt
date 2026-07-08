package com.lifeosai.app.di

import com.lifeosai.app.data.repository.LifeOSRepositoryImpl
import com.lifeosai.app.domain.repository.LifeOSRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLifeOSRepository(
        lifeOSRepositoryImpl: LifeOSRepositoryImpl
    ): LifeOSRepository

    @Binds
    @Singleton
    abstract fun bindMemoryRepository(
        memoryRepositoryImpl: com.lifeosai.app.data.repository.MemoryRepositoryImpl
    ): com.lifeosai.app.domain.repository.MemoryRepository

    @Binds
    @Singleton
    abstract fun bindBrainRepository(
        brainRepositoryImpl: com.lifeosai.app.data.repository.BrainRepositoryImpl
    ): com.lifeosai.app.domain.repository.BrainRepository

    @Binds
    @Singleton
    abstract fun bindCaptureRepository(
        captureRepositoryImpl: com.lifeosai.app.data.repository.CaptureRepositoryImpl
    ): com.lifeosai.app.domain.repository.CaptureRepository

    @Binds
    @Singleton
    abstract fun bindFlowRepository(
        flowRepositoryImpl: com.lifeosai.app.data.repository.FlowRepositoryImpl
    ): com.lifeosai.app.domain.repository.FlowRepository
}

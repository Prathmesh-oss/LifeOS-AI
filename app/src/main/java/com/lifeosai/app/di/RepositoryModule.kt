package com.lifeosai.app.di

import com.lifeosai.app.data.repository.*
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
    
    // Additional domain-specific repositories can be bound here if interfaces are defined in domain
}

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
    
    // Bindings for other repositories can be added here once their interfaces are in the domain layer
}

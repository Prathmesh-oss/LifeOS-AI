package com.lifeosai.app.di

import com.lifeosai.app.ai.planner.repository.PlannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlannerModule {

    @Provides
    @Singleton
    fun providePlannerRepository(): PlannerRepository {
        return object : PlannerRepository {
            override fun observePlan(date: java.time.LocalDate) = flow { emit(null) }
            override suspend fun savePlan(plan: com.lifeosai.app.ai.planner.model.FlightPlan) {}
            override suspend fun deletePlan(planId: String) {}
        }
    }
}

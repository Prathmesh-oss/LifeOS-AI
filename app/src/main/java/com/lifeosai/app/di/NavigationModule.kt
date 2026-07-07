package com.lifeosai.app.di

import com.lifeosai.app.navigation.LifeOSNavigator
import com.lifeosai.app.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(navigator: LifeOSNavigator): Navigator
}

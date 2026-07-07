package com.lifeosai.app.di

import android.content.Context
import androidx.room.Room
import com.lifeosai.app.data.datasource.local.LifeOSDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LifeOSDatabase {
        return Room.databaseBuilder(
            context,
            LifeOSDatabase::class.java,
            LifeOSDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideUserDao(db: LifeOSDatabase) = db.userDao()

    @Provides
    fun provideTaskDao(db: LifeOSDatabase) = db.taskDao()

    @Provides
    fun provideMemoryDao(db: LifeOSDatabase) = db.memoryDao()

    @Provides
    fun provideCalendarDao(db: LifeOSDatabase) = db.calendarDao()

    @Provides
    fun provideDNADao(db: LifeOSDatabase) = db.dnaDao()

    @Provides
    fun provideNotificationDao(db: LifeOSDatabase) = db.notificationDao()
}

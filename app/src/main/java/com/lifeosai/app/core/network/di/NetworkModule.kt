package com.lifeosai.app.core.network.di

import com.lifeosai.app.core.network.NetworkEnvironment
import com.lifeosai.app.core.network.api.GeminiApiService
import com.lifeosai.app.core.network.api.LifeOSApiService
import com.lifeosai.app.core.network.interceptor.AuthInterceptor
import com.lifeosai.app.core.network.interceptor.RetryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        retryInterceptor: RetryInterceptor
    ): OkHttpClient {
        val environment = NetworkEnvironment.DEVELOPMENT // Should be injected or use .current()

        return OkHttpClient.Builder()
            .connectTimeout(environment.timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(environment.timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(environment.timeoutSeconds, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(retryInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("LifeOSRetrofit")
    fun provideLifeOSRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val environment = NetworkEnvironment.DEVELOPMENT
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(environment.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    @Named("GeminiRetrofit")
    fun provideGeminiRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val environment = NetworkEnvironment.DEVELOPMENT
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(environment.geminiUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideLifeOSApiService(@Named("LifeOSRetrofit") retrofit: Retrofit): LifeOSApiService {
        return retrofit.create(LifeOSApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(@Named("GeminiRetrofit") retrofit: Retrofit): GeminiApiService {
        return retrofit.create(GeminiApiService::class.java)
    }
}

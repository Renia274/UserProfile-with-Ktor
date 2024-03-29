package com.example.practice.module

import android.app.Application
import com.example.practice.ktor.services.PostsService
import com.example.practice.ktor.services.PostsServiceImpl
import com.example.practice.services.CrashlyticsService
import com.example.practice.services.FirebaseAnalyticsService
import com.example.practice.services.FirebaseAuthService
import com.example.practice.services.FirebaseAuthServiceImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePostsService(): PostsService {
        return PostsServiceImpl(
            client = HttpClient(Android) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthService(firebaseAuth: FirebaseAuth): FirebaseAuthService {
        return FirebaseAuthServiceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Provides
    @Singleton
    fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalyticsService(): FirebaseAnalyticsService {
        return FirebaseAnalyticsService
    }

    @Provides
    @Singleton
    fun provideCrashlyticsService(): CrashlyticsService {
        return CrashlyticsService
    }

}

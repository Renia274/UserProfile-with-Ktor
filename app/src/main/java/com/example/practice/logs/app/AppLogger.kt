package com.example.practice.logs.app

import android.os.Bundle
import com.example.practice.services.CrashlyticsService
import com.example.practice.services.FirebaseAnalyticsService
import com.google.firebase.analytics.FirebaseAnalytics

object AppLogger {
    private var isInitialized = false
    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun initialize() {
        if (!isInitialized) {
            CrashlyticsService.initialize()
            firebaseAnalytics?.let { FirebaseAnalyticsService.initialize(it) }
            isInitialized = true
        }
    }

    fun logEvent(eventName: String, params: Bundle? = null) {
        if (!isInitialized) {
            throw IllegalStateException("AppLogger has not been initialized. Call initialize() first.")
        }
        FirebaseAnalyticsService.logAnalyticsEvent(eventName, params)
        CrashlyticsService.logCrashlyticsEvent(eventName)
    }

    fun logError(errorMessage: String, throwable: Throwable? = null) {
        if (!isInitialized) {
            throw IllegalStateException("AppLogger has not been initialized. Call initialize() first.")
        }
        CrashlyticsService.logCrashlyticsEvent(errorMessage)
        throwable?.let { CrashlyticsService.logCrashlyticsThrowable(it) }
    }



    fun logDeepLinkEvent(deepLink: String) {
        logEvent("DeepLinkOpened", Bundle().apply {
            putString("DeepLink", deepLink)
        })
    }

}

package com.example.practice.services

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsService {
    private var firebaseCrashlytics: FirebaseCrashlytics? = null

    fun initialize() {
        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
    }

    fun logCrashlyticsEvent(eventName: String) {
        firebaseCrashlytics?.log(eventName)
    }

    fun logCrashlyticsThrowable(throwable: Throwable) {
        firebaseCrashlytics?.recordException(throwable)
    }
}

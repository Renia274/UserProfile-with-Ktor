package com.example.practice.services

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object FirebaseAnalyticsService {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun initialize(firebaseAnalytics: FirebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics
    }

    fun logAnalyticsEvent(eventName: String, params: Bundle? = null) {
        firebaseAnalytics?.logEvent(eventName, params)
    }
}

package com.example.practice.logs.navigation

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import android.content.Context
import com.example.practice.services.FirebaseAnalyticsService

object NavigationAnalyticsLogger {
    private lateinit var firebaseAnalyticsService: FirebaseAnalyticsService

    fun initialize(context: Context) {
        firebaseAnalyticsService = FirebaseAnalyticsService
        FirebaseAnalyticsService.initialize(FirebaseAnalytics.getInstance(context))
    }

    fun logNavigationEvent(route: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, route)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, route)
        }
        firebaseAnalyticsService.logAnalyticsEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }


    fun logAnalyticsDeepLinkEvent(url: String) {
        val bundle = Bundle().apply {
            putString("deep_link_url", url)
        }
        firebaseAnalyticsService.logAnalyticsEvent("deep_link_button_click", bundle)
    }


}

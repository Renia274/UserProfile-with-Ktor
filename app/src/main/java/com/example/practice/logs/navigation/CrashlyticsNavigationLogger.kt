package com.example.practice.logs.navigation

import android.os.Bundle
import com.example.practice.services.CrashlyticsService

object CrashlyticsNavigationLogger {

    fun initialize() {
        CrashlyticsService.initialize()
    }

    fun logNavigationEvent(route: String) {
        CrashlyticsService.logCrashlyticsEvent("Navigated to: $route")
    }

    fun logCrashlyticsDeepLinkEvent(url: String) {
        CrashlyticsService.logCrashlyticsEvent("Deep link not opened: $url")
    }



}

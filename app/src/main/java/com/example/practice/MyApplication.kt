package com.example.practice

import android.app.Application
import com.example.practice.logs.app.AppLogger
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLogger.initialize()
    }
}

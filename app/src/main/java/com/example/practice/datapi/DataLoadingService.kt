package com.example.practice.datapi

import android.content.Context
import android.content.res.Resources
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope



class DataLoadingService(private val resources: Resources) {
    fun getDataLoadingApi(): DataLoadingApi {
        return JsonDataLoadingApi(resources)
    }
}


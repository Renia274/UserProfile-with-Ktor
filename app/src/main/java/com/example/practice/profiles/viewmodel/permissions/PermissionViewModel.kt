package com.example.practice.profiles.viewmodel.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {
    private val cameraPermissionState = mutableStateOf(false)
    private val micPermissionState = mutableStateOf(false)

    val cameraPermissionResult: Boolean
        get() = cameraPermissionState.value

    val micPermissionResult: Boolean
        get() = micPermissionState.value

    fun cameraPermissionResult(isGranted: Boolean) {
        cameraPermissionState.value = isGranted
    }

    fun micPermissionResult(isGranted: Boolean) {
        micPermissionState.value = isGranted
    }
}

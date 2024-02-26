package com.example.practice.profiles.viewmodel.permissions

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class PermissionState(
    val cameraPermissionResult: Boolean,
    val micPermissionResult: Boolean
)

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {

    private val permissionState = MutableStateFlow(PermissionState(false, false))
    val permissionStateFlow = permissionState.asStateFlow()

    fun updatePermissionState(
        cameraPermissionResult: Boolean? = null,
        micPermissionResult: Boolean? = null
    ) {
        permissionState.value = permissionState.value.copy(
            cameraPermissionResult = cameraPermissionResult ?: permissionState.value.cameraPermissionResult,
            micPermissionResult = micPermissionResult ?: permissionState.value.micPermissionResult
        )
    }

    fun setCameraPermissionResult(isGranted: Boolean) {
        updatePermissionState(cameraPermissionResult = isGranted)
    }

    fun setMicPermissionResult(isGranted: Boolean) {
        updatePermissionState(micPermissionResult = isGranted)
    }
}

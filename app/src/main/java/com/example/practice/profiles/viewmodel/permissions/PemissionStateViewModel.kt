package com.example.practice.profiles.viewmodel.permissions

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class PermissionState(
    val isCameraPermissionDialogShown: Boolean,
    val isMicrophonePermissionDialogShown: Boolean
)



@HiltViewModel
class PermissionStateViewModel @Inject constructor() : ViewModel() {

    var permissionState = MutableStateFlow(
        PermissionState(
            isCameraPermissionDialogShown = false,
            isMicrophonePermissionDialogShown = false
        )
    )

    fun setCameraPermissionDialogShown(value: Boolean) {
        permissionState.value =
            permissionState.value.copy(isCameraPermissionDialogShown = value)
    }

    fun setMicrophonePermissionDialogShown(value: Boolean) {
        permissionState.value =
            permissionState.value.copy(isMicrophonePermissionDialogShown = value)
    }
}

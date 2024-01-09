package com.example.practice.profiles.viewmodel.permissions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionStateViewModel @Inject constructor() : ViewModel() {
    private val _isCameraPermissionDialogShown = mutableStateOf(false)
    val isCameraPermissionDialogShown: State<Boolean> = _isCameraPermissionDialogShown

    private val _isMicrophonePermissionDialogShown = mutableStateOf(false)
    val isMicrophonePermissionDialogShown: State<Boolean> = _isMicrophonePermissionDialogShown

    private val _arePermissionDialogsShown = mutableStateOf(false)
    val arePermissionDialogsShown: State<Boolean> = _arePermissionDialogsShown

    fun setCameraPermissionDialogShown(value: Boolean) {
        _isCameraPermissionDialogShown.value = value
        _arePermissionDialogsShown.value = value
    }

    fun setMicrophonePermissionDialogShown(value: Boolean) {
        _isMicrophonePermissionDialogShown.value = value
        _arePermissionDialogsShown.value = value
    }
}

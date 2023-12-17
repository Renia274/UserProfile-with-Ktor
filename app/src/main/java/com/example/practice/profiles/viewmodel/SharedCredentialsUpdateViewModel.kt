package com.example.practice.profiles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedCredentialsUpdateViewModel @Inject constructor() : ViewModel() {

    private val _updatedCredentials = MutableLiveData<Pair<String, String>>()
    val updatedCredentials: LiveData<Pair<String, String>>
        get() = _updatedCredentials

    fun updateCredentials(username: String, password: String) {
        _updatedCredentials.value = Pair(username, password)
    }
}

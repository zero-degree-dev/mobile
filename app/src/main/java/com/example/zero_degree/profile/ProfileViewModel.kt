package com.example.zero_degree.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val _username = MutableStateFlow<String>("")
    val username: Flow<String> = _username.asStateFlow()

    fun loadUser() {
        _username.value = "Егор" // пример
    }
}
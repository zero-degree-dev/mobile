package com.example.zero_degree.features.profile.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.model.User
import com.example.zero_degree.core.api.TokenManager
import com.example.zero_degree.features.profile.api.ProfileRepository
import com.example.zero_degree.features.profile.impl.ProfileRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val context = application.applicationContext
    private val repository: ProfileRepository = ProfileRepositoryImpl(context)
    
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()
    
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    
    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess = _logoutSuccess.asStateFlow()
    
    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess = _deleteSuccess.asStateFlow()
    
    fun loadUser() {
        viewModelScope.launch {
            val userId = TokenManager.getUserId(context)
            userId?.let {
                repository.getUser(it).fold(
                    onSuccess = { userData ->
                        _user.value = userData
                        _username.value = userData.name
                    },
                    onFailure = { }
                )
            }
        }
    }
    
    fun logout(context: android.content.Context) {
        TokenManager.clearToken(context)
        _logoutSuccess.value = true
    }
    
    fun deleteAccount(context: android.content.Context) {
        viewModelScope.launch {
            val userId = TokenManager.getUserId(context)
            userId?.let {
                repository.deleteUser(it).fold(
                    onSuccess = {
                        TokenManager.clearToken(context)
                        _deleteSuccess.value = true
                    },
                    onFailure = { }
                )
            }
        }
    }
    
    fun resetLogoutSuccess() {
        _logoutSuccess.value = false
    }
    
    fun resetDeleteSuccess() {
        _deleteSuccess.value = false
    }
}


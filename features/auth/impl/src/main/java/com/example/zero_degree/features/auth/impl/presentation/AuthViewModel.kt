package com.example.zero_degree.features.auth.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.TokenManager
import com.example.zero_degree.features.auth.api.AuthRepository
import com.example.zero_degree.features.auth.impl.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val context = application.applicationContext
    private val repository: AuthRepository = AuthRepositoryImpl(context)
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()
    
    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess = _registerSuccess.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.login(email, password).fold(
                onSuccess = { response ->
                    // Сохраняем токен
                    TokenManager.saveToken(context, response.access_token)
                    TokenManager.saveUserId(context, response.user.id)
                    _loginSuccess.value = true
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Неверный email или пароль"
                }
            )
            _isLoading.value = false
        }
    }
    
    fun register(name: String, email: String, password: String, avatarUrl: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.register(name, email, password, avatarUrl).fold(
                onSuccess = { response ->
                    // Сохраняем токен
                    TokenManager.saveToken(context, response.access_token)
                    TokenManager.saveUserId(context, response.user.id)
                    _registerSuccess.value = true
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Ошибка регистрации. Возможно, такой email уже зарегистрирован"
                }
            )
            _isLoading.value = false
        }
    }
    
    fun resetLoginSuccess() {
        _loginSuccess.value = false
    }
    
    fun resetRegisterSuccess() {
        _registerSuccess.value = false
    }
}


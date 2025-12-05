package com.example.zero_degree.data.repository

import com.example.zero_degree.data.api.RetrofitClient
import com.example.zero_degree.data.model.AuthResponse
import com.example.zero_degree.data.model.LoginRequest
import com.example.zero_degree.data.model.RegisterRequest
import com.example.zero_degree.data.model.User

// Репозиторий для авторизации
class AuthRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // Вход
    suspend fun login(email: String, password: String): AuthResponse? {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    // Регистрация
    suspend fun register(email: String, password: String, name: String, age: Int): AuthResponse? {
        return try {
            val request = RegisterRequest(email, password, name, age)
            val response = apiService.register(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    // Получить профиль пользователя
    suspend fun getUserProfile(): User? {
        return try {
            val response = apiService.getUserProfile()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}




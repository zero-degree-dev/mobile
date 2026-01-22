package com.example.zero_degree.features.auth.impl

import android.content.Context
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.AuthResponse
import com.example.zero_degree.core.api.model.LoginRequest
import com.example.zero_degree.core.api.model.RegisterRequest
import com.example.zero_degree.features.auth.api.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val context: Context) : AuthRepository {
    
    private val apiService = RetrofitClient.getApiService(context)
    
    override suspend fun login(email: String, password: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Неверный email или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun register(name: String, email: String, password: String, avatarUrl: String?): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val request = RegisterRequest(name, email, password, avatarUrl)
            val response = apiService.register(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Ошибка регистрации: ${response.message()}. $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
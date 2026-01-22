package com.example.zero_degree.features.profile.impl

import android.content.Context
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.User
import com.example.zero_degree.features.profile.api.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(private val context: Context) : ProfileRepository {
    
    private val apiService = RetrofitClient.getApiService(context)
    
    override suspend fun getUser(id: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Не удалось загрузить профиль пользователя"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUser(id: String, user: User): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateUser(id, user)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Не удалось обновить профиль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteUser(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteUser(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Не удалось удалить аккаунт"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


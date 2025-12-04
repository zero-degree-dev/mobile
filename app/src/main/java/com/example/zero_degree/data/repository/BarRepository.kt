package com.example.zero_degree.data.repository

import com.example.zero_degree.data.api.RetrofitClient
import com.example.zero_degree.data.model.Bar

// Репозиторий для работы с барами
class BarRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // Получить все бары
    suspend fun getBars(): List<Bar> {
        return try {
            val response = apiService.getBars()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Получить бар по ID
    suspend fun getBarById(id: Int): Bar? {
        return try {
            val response = apiService.getBarById(id)
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



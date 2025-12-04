package com.example.zero_degree.data.repository

import com.example.zero_degree.data.api.RetrofitClient
import com.example.zero_degree.data.model.Event

// Репозиторий для работы с событиями
class EventRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // Получить все события
    suspend fun getEvents(): List<Event> {
        return try {
            val response = apiService.getEvents()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Зарегистрироваться на событие
    suspend fun registerForEvent(eventId: Int): Boolean {
        return try {
            val response = apiService.registerForEvent(eventId)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}



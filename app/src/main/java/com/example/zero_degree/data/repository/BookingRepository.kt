package com.example.zero_degree.data.repository

import com.example.zero_degree.data.api.RetrofitClient
import com.example.zero_degree.data.model.Booking

// Репозиторий для работы с бронированиями
class BookingRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // Получить все бронирования пользователя
    suspend fun getBookings(): List<Booking> {
        return try {
            val response = apiService.getBookings()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Создать бронирование
    suspend fun createBooking(booking: Booking): Booking? {
        return try {
            val response = apiService.createBooking(booking)
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



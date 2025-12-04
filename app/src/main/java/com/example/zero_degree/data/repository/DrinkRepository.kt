package com.example.zero_degree.data.repository

import com.example.zero_degree.data.api.RetrofitClient
import com.example.zero_degree.data.model.Drink
import com.example.zero_degree.data.model.Review

// Репозиторий для работы с напитками
class DrinkRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // Получить все напитки с фильтрами
    suspend fun getDrinks(type: String? = null, taste: String? = null): List<Drink> {
        return try {
            val response = apiService.getDrinks(type, taste)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // В случае ошибки возвращаем пустой список
            emptyList()
        }
    }
    
    // Получить напиток по ID
    suspend fun getDrinkById(id: Int): Drink? {
        return try {
            val response = apiService.getDrinkById(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    // Получить отзывы для напитка
    suspend fun getDrinkReviews(drinkId: Int): List<Review> {
        return try {
            val response = apiService.getDrinkReviews(drinkId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Добавить отзыв
    suspend fun addReview(drinkId: Int, review: Review): Review? {
        return try {
            val response = apiService.addReview(drinkId, review)
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



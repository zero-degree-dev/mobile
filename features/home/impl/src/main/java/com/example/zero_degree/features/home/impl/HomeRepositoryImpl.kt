package com.example.zero_degree.features.home.impl

import android.content.Context
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.api.model.Event
import com.example.zero_degree.core.api.model.User
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.features.bars.api.BarRepository
import com.example.zero_degree.features.bars.impl.BarRepositoryImpl
import com.example.zero_degree.features.drinks.api.DrinkRepository
import com.example.zero_degree.features.drinks.impl.DrinkRepositoryImpl
import com.example.zero_degree.features.events.api.EventRepository
import com.example.zero_degree.features.events.impl.EventRepositoryImpl
import com.example.zero_degree.features.home.api.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(private val context: Context) : HomeRepository {
    
    private val apiService = RetrofitClient.getApiService(context)
    private val barRepository: BarRepository = BarRepositoryImpl(context)
    private val drinkRepository: DrinkRepository = DrinkRepositoryImpl(context)
    private val eventRepository: EventRepository = EventRepositoryImpl(context)
    
    override suspend fun getCurrentUser(): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCurrentUser()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Не удалось загрузить профиль пользователя"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getRecentBars(): Result<List<Bar>> = withContext(Dispatchers.IO) {
        barRepository.getBars().fold(
            onSuccess = { bars -> Result.success(bars.take(3)) },
            onFailure = { exception -> Result.failure(exception) }
        )
    }
    
    override suspend fun getRecentDrinks(): Result<List<Drink>> = withContext(Dispatchers.IO) {
        drinkRepository.getDrinks(available = true).fold(
            onSuccess = { drinks -> Result.success(drinks.take(3)) },
            onFailure = { exception -> Result.failure(exception) }
        )
    }
    
    override suspend fun getActiveEvents(): Result<List<Event>> = withContext(Dispatchers.IO) {
        eventRepository.getEvents().fold(
            onSuccess = { events -> Result.success(events.take(3)) },
            onFailure = { exception -> Result.failure(exception) }
        )
    }
}


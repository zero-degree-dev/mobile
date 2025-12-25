package com.example.zero_degree.features.drinks.impl

import android.content.Context
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.storage.mapper.DrinkMapper
import com.example.zero_degree.features.drinks.api.DrinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrinkRepositoryImpl(private val context: Context) : DrinkRepository {
    
    private val apiService = RetrofitClient.getApiService(context)
    private val database = AppDatabase.getDatabase(context)
    private val drinkDao = database.drinkDao()
    
    override suspend fun getDrinks(
        type: String?,
        name: String?,
        available: Boolean?
    ): Result<List<Drink>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDrinks(type, name, available)
            if (response.isSuccessful && response.body() != null) {
                val drinks = response.body()!!
                drinkDao.insertDrinks(DrinkMapper.toEntityList(drinks))
                Result.success(drinks)
            } else {
                val cachedDrinks = drinkDao.getAllDrinks()
                if (cachedDrinks.isNotEmpty()) {
                    Result.success(DrinkMapper.toModelList(cachedDrinks))
                } else {
                    Result.failure(Exception("Не удалось загрузить список напитков"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedDrinks = drinkDao.getAllDrinks()
                if (cachedDrinks.isNotEmpty()) {
                    Result.success(DrinkMapper.toModelList(cachedDrinks))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getDrinkById(id: String): Result<Drink> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDrinkById(id)
            if (response.isSuccessful && response.body() != null) {
                val drink = response.body()!!
                drinkDao.insertDrink(DrinkMapper.toEntity(drink))
                Result.success(drink)
            } else {
                val cachedDrink = drinkDao.getDrinkById(id)
                if (cachedDrink != null) {
                    Result.success(DrinkMapper.toModel(cachedDrink))
                } else {
                    Result.failure(Exception("Не удалось загрузить информацию о напитке"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedDrink = drinkDao.getDrinkById(id)
                if (cachedDrink != null) {
                    Result.success(DrinkMapper.toModel(cachedDrink))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
}


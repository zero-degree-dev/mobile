package com.example.zero_degree.features.drinks.impl

import android.content.Context
import android.util.Log
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.storage.mapper.DrinkMapper
import com.example.zero_degree.features.drinks.api.DrinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrinkRepositoryImpl(private val context: Context) : DrinkRepository {
    
    companion object {
        private const val TAG = "DrinkRepositoryImpl"
    }
    
    private val apiService = RetrofitClient.getApiService(context)
    private val database = AppDatabase.getDatabase(context)
    private val drinkDao = database.drinkDao()
    
    override suspend fun getDrinks(
        type: String?,
        name: String?,
        available: Boolean?
    ): Result<List<Drink>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getDrinks: Запрос к API (type=$type, name=$name, available=$available)")
            val response = apiService.getDrinks(type, name, available)
            Log.d(TAG, "getDrinks: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val drinks = response.body()!!
                Log.d(TAG, "getDrinks: Получено ${drinks.size} напитков из API")
                
                try {
                    val entities = DrinkMapper.toEntityList(drinks)
                    Log.d(TAG, "getDrinks: Конвертировано ${entities.size} entities для сохранения")
                    drinkDao.insertDrinks(entities)
                    Log.d(TAG, "getDrinks: Данные успешно сохранены в БД")
                    
                    val savedCount = drinkDao.getAllDrinks().size
                    Log.d(TAG, "getDrinks: Проверка БД - сохранено $savedCount напитков")
                } catch (dbException: Exception) {
                    Log.e(TAG, "getDrinks: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(drinks)
            } else {
                Log.w(TAG, "getDrinks: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedDrinks = drinkDao.getAllDrinks()
                Log.d(TAG, "getDrinks: Найдено ${cachedDrinks.size} напитков в кэше")
                if (cachedDrinks.isNotEmpty()) {
                    Result.success(DrinkMapper.toModelList(cachedDrinks))
                } else {
                    Result.failure(Exception("Не удалось загрузить список напитков"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getDrinks: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getDrinks: Пытаемся загрузить из кэша после ошибки")
                val cachedDrinks = drinkDao.getAllDrinks()
                Log.d(TAG, "getDrinks: Найдено ${cachedDrinks.size} напитков в кэше")
                if (cachedDrinks.isNotEmpty()) {
                    Result.success(DrinkMapper.toModelList(cachedDrinks))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getDrinks: Ошибка при чтении из БД", dbException)
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getDrinkById(id: String): Result<Drink> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getDrinkById: Запрос к API для напитка id=$id")
            val response = apiService.getDrinkById(id)
            Log.d(TAG, "getDrinkById: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val drink = response.body()!!
                Log.d(TAG, "getDrinkById: Получен напиток из API: ${drink.name}")
                
                try {
                    val entity = DrinkMapper.toEntity(drink)
                    Log.d(TAG, "getDrinkById: Конвертировано в entity: id=${entity.id}, name=${entity.name}")
                    drinkDao.insertDrink(entity)
                    Log.d(TAG, "getDrinkById: Напиток успешно сохранен в БД")
                    
                    val savedDrink = drinkDao.getDrinkById(id)
                    if (savedDrink != null) {
                        Log.d(TAG, "getDrinkById: Проверка БД - напиток найден: ${savedDrink.name}")
                    } else {
                        Log.w(TAG, "getDrinkById: ВНИМАНИЕ! Напиток не найден в БД после сохранения!")
                    }
                } catch (dbException: Exception) {
                    Log.e(TAG, "getDrinkById: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(drink)
            } else {
                Log.w(TAG, "getDrinkById: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedDrink = drinkDao.getDrinkById(id)
                if (cachedDrink != null) {
                    Log.d(TAG, "getDrinkById: Найден напиток в кэше: ${cachedDrink.name}")
                    Result.success(DrinkMapper.toModel(cachedDrink))
                } else {
                    Log.w(TAG, "getDrinkById: Напиток не найден в кэше")
                    Result.failure(Exception("Не удалось загрузить информацию о напитке"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getDrinkById: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getDrinkById: Пытаемся загрузить из кэша после ошибки")
                val cachedDrink = drinkDao.getDrinkById(id)
                if (cachedDrink != null) {
                    Log.d(TAG, "getDrinkById: Найден напиток в кэше: ${cachedDrink.name}")
                    Result.success(DrinkMapper.toModel(cachedDrink))
                } else {
                    Log.w(TAG, "getDrinkById: Напиток не найден в кэше")
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getDrinkById: Ошибка при чтении из БД", dbException)
                Result.failure(e)
            }
        }
    }
}


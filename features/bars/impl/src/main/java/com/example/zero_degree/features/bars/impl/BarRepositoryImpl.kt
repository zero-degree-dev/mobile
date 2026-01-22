package com.example.zero_degree.features.bars.impl

import android.content.Context
import android.util.Log
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.storage.mapper.BarMapper
import com.example.zero_degree.features.bars.api.BarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BarRepositoryImpl(private val context: Context) : BarRepository {
    
    companion object {
        private const val TAG = "BarRepositoryImpl"
    }
    
    private val apiService = RetrofitClient.getApiService(context)
    private val database = AppDatabase.getDatabase(context)
    private val barDao = database.barDao()
    
    override suspend fun getBars(): Result<List<Bar>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getBars: Запрос к API...")
            val response = apiService.getBars()
            Log.d(TAG, "getBars: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val bars = response.body()!!
                Log.d(TAG, "getBars: Получено ${bars.size} баров из API")
                
                try {
                    val entities = BarMapper.toEntityList(bars)
                    Log.d(TAG, "getBars: Конвертировано ${entities.size} entities для сохранения")
                    barDao.insertBars(entities)
                    Log.d(TAG, "getBars: Данные успешно сохранены в БД")
                    
                    // Проверяем, что данные действительно сохранились
                    val savedCount = barDao.getAllBars().size
                    Log.d(TAG, "getBars: Проверка БД - сохранено $savedCount баров")
                } catch (dbException: Exception) {
                    Log.e(TAG, "getBars: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(bars)
            } else {
                Log.w(TAG, "getBars: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedBars = barDao.getAllBars()
                Log.d(TAG, "getBars: Найдено ${cachedBars.size} баров в кэше")
                if (cachedBars.isNotEmpty()) {
                    Result.success(BarMapper.toModelList(cachedBars))
                } else {
                    Result.failure(Exception("Не удалось загрузить список баров"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getBars: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getBars: Пытаемся загрузить из кэша после ошибки")
                val cachedBars = barDao.getAllBars()
                Log.d(TAG, "getBars: Найдено ${cachedBars.size} баров в кэше")
                if (cachedBars.isNotEmpty()) {
                    Result.success(BarMapper.toModelList(cachedBars))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getBars: Ошибка при чтении из БД", dbException)
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getBarById(id: String): Result<Bar> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getBarById: Запрос к API для бара id=$id")
            val response = apiService.getBarById(id)
            Log.d(TAG, "getBarById: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val bar = response.body()!!
                Log.d(TAG, "getBarById: Получен бар из API: ${bar.name}")
                
                try {
                    val entity = BarMapper.toEntity(bar)
                    Log.d(TAG, "getBarById: Конвертировано в entity: id=${entity.id}, name=${entity.name}")
                    barDao.insertBar(entity)
                    Log.d(TAG, "getBarById: Бар успешно сохранен в БД")
                    
                    // Проверяем сохранение
                    val savedBar = barDao.getBarById(id)
                    if (savedBar != null) {
                        Log.d(TAG, "getBarById: Проверка БД - бар найден: ${savedBar.name}")
                    } else {
                        Log.w(TAG, "getBarById: ВНИМАНИЕ! Бар не найден в БД после сохранения!")
                    }
                } catch (dbException: Exception) {
                    Log.e(TAG, "getBarById: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(bar)
            } else {
                Log.w(TAG, "getBarById: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedBar = barDao.getBarById(id)
                if (cachedBar != null) {
                    Log.d(TAG, "getBarById: Найден бар в кэше: ${cachedBar.name}")
                    Result.success(BarMapper.toModel(cachedBar))
                } else {
                    Log.w(TAG, "getBarById: Бар не найден в кэше")
                    Result.failure(Exception("Не удалось загрузить информацию о баре"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getBarById: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getBarById: Пытаемся загрузить из кэша после ошибки")
                val cachedBar = barDao.getBarById(id)
                if (cachedBar != null) {
                    Log.d(TAG, "getBarById: Найден бар в кэше: ${cachedBar.name}")
                    Result.success(BarMapper.toModel(cachedBar))
                } else {
                    Log.w(TAG, "getBarById: Бар не найден в кэше")
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getBarById: Ошибка при чтении из БД", dbException)
                Result.failure(e)
            }
        }
    }
}


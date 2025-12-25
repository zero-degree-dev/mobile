package com.example.zero_degree.features.bars.impl

import android.content.Context
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.storage.mapper.BarMapper
import com.example.zero_degree.features.bars.api.BarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BarRepositoryImpl(private val context: Context) : BarRepository {
    
    private val apiService = RetrofitClient.getApiService(context)
    private val database = AppDatabase.getDatabase(context)
    private val barDao = database.barDao()
    
    override suspend fun getBars(): Result<List<Bar>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBars()
            if (response.isSuccessful && response.body() != null) {
                val bars = response.body()!!
                barDao.insertBars(BarMapper.toEntityList(bars))
                Result.success(bars)
            } else {
                val cachedBars = barDao.getAllBars()
                if (cachedBars.isNotEmpty()) {
                    Result.success(BarMapper.toModelList(cachedBars))
                } else {
                    Result.failure(Exception("Не удалось загрузить список баров"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedBars = barDao.getAllBars()
                if (cachedBars.isNotEmpty()) {
                    Result.success(BarMapper.toModelList(cachedBars))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getBarById(id: String): Result<Bar> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBarById(id)
            if (response.isSuccessful && response.body() != null) {
                val bar = response.body()!!
                barDao.insertBar(BarMapper.toEntity(bar))
                Result.success(bar)
            } else {
                val cachedBar = barDao.getBarById(id)
                if (cachedBar != null) {
                    Result.success(BarMapper.toModel(cachedBar))
                } else {
                    Result.failure(Exception("Не удалось загрузить информацию о баре"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedBar = barDao.getBarById(id)
                if (cachedBar != null) {
                    Result.success(BarMapper.toModel(cachedBar))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
}


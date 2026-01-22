package com.example.zero_degree.features.events.impl

import android.content.Context
import android.util.Log
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.Event
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.storage.mapper.EventMapper
import com.example.zero_degree.features.events.api.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepositoryImpl(private val context: Context) : EventRepository {
    
    companion object {
        private const val TAG = "EventRepositoryImpl"
    }
    
    private val apiService = RetrofitClient.getApiService(context)
    private val database = AppDatabase.getDatabase(context)
    private val eventDao = database.eventDao()
    
    override suspend fun getEvents(barId: String?, date: String?): Result<List<Event>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getEvents: Запрос к API (barId=$barId, date=$date)")
            val response = apiService.getEvents(barId, date)
            Log.d(TAG, "getEvents: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val events = response.body()!!
                Log.d(TAG, "getEvents: Получено ${events.size} событий из API")
                
                try {
                    val entities = EventMapper.toEntityList(events)
                    Log.d(TAG, "getEvents: Конвертировано ${entities.size} entities для сохранения")
                    eventDao.insertEvents(entities)
                    Log.d(TAG, "getEvents: Данные успешно сохранены в БД")
                    
                    val savedCount = eventDao.getAllEvents().size
                    Log.d(TAG, "getEvents: Проверка БД - сохранено $savedCount событий")
                } catch (dbException: Exception) {
                    Log.e(TAG, "getEvents: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(events)
            } else {
                Log.w(TAG, "getEvents: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedEvents = if (barId != null) {
                    eventDao.getEventsByBarId(barId)
                } else {
                    eventDao.getAllEvents()
                }
                Log.d(TAG, "getEvents: Найдено ${cachedEvents.size} событий в кэше")
                if (cachedEvents.isNotEmpty()) {
                    Result.success(EventMapper.toModelList(cachedEvents))
                } else {
                    Result.failure(Exception("Не удалось загрузить список событий"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getEvents: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getEvents: Пытаемся загрузить из кэша после ошибки")
                val cachedEvents = if (barId != null) {
                    eventDao.getEventsByBarId(barId)
                } else {
                    eventDao.getAllEvents()
                }
                Log.d(TAG, "getEvents: Найдено ${cachedEvents.size} событий в кэше")
                if (cachedEvents.isNotEmpty()) {
                    Result.success(EventMapper.toModelList(cachedEvents))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getEvents: Ошибка при чтении из БД", dbException)
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getEventById(id: String): Result<Event> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getEventById: Запрос к API для события id=$id")
            val response = apiService.getEventById(id)
            Log.d(TAG, "getEventById: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val event = response.body()!!
                Log.d(TAG, "getEventById: Получено событие из API: ${event.name}")
                
                try {
                    val entity = EventMapper.toEntity(event)
                    Log.d(TAG, "getEventById: Конвертировано в entity: id=${entity.id}, name=${entity.name}")
                    eventDao.insertEvent(entity)
                    Log.d(TAG, "getEventById: Событие успешно сохранено в БД")
                    
                    val savedEvent = eventDao.getEventById(id)
                    if (savedEvent != null) {
                        Log.d(TAG, "getEventById: Проверка БД - событие найдено: ${savedEvent.name}")
                    } else {
                        Log.w(TAG, "getEventById: ВНИМАНИЕ! Событие не найдено в БД после сохранения!")
                    }
                } catch (dbException: Exception) {
                    Log.e(TAG, "getEventById: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(event)
            } else {
                Log.w(TAG, "getEventById: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedEvent = eventDao.getEventById(id)
                if (cachedEvent != null) {
                    Log.d(TAG, "getEventById: Найдено событие в кэше: ${cachedEvent.name}")
                    Result.success(EventMapper.toModel(cachedEvent))
                } else {
                    Log.w(TAG, "getEventById: Событие не найдено в кэше")
                    Result.failure(Exception("Не удалось загрузить информацию о событии"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getEventById: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getEventById: Пытаемся загрузить из кэша после ошибки")
                val cachedEvent = eventDao.getEventById(id)
                if (cachedEvent != null) {
                    Log.d(TAG, "getEventById: Найдено событие в кэше: ${cachedEvent.name}")
                    Result.success(EventMapper.toModel(cachedEvent))
                } else {
                    Log.w(TAG, "getEventById: Событие не найдено в кэше")
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getEventById: Ошибка при чтении из БД", dbException)
                Result.failure(e)
            }
        }
    }
}


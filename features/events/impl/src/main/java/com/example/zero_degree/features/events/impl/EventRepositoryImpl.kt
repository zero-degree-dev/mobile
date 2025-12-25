package com.example.zero_degree.features.events.impl

import android.content.Context
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.Event
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.storage.mapper.EventMapper
import com.example.zero_degree.features.events.api.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepositoryImpl(private val context: Context) : EventRepository {
    
    private val apiService = RetrofitClient.getApiService(context)
    private val database = AppDatabase.getDatabase(context)
    private val eventDao = database.eventDao()
    
    override suspend fun getEvents(barId: String?, date: String?): Result<List<Event>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getEvents(barId, date)
            if (response.isSuccessful && response.body() != null) {
                val events = response.body()!!
                eventDao.insertEvents(EventMapper.toEntityList(events))
                Result.success(events)
            } else {
                val cachedEvents = if (barId != null) {
                    eventDao.getEventsByBarId(barId)
                } else {
                    eventDao.getAllEvents()
                }
                if (cachedEvents.isNotEmpty()) {
                    Result.success(EventMapper.toModelList(cachedEvents))
                } else {
                    Result.failure(Exception("Не удалось загрузить список событий"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedEvents = if (barId != null) {
                    eventDao.getEventsByBarId(barId)
                } else {
                    eventDao.getAllEvents()
                }
                if (cachedEvents.isNotEmpty()) {
                    Result.success(EventMapper.toModelList(cachedEvents))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getEventById(id: String): Result<Event> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getEventById(id)
            if (response.isSuccessful && response.body() != null) {
                val event = response.body()!!
                eventDao.insertEvent(EventMapper.toEntity(event))
                Result.success(event)
            } else {
                val cachedEvent = eventDao.getEventById(id)
                if (cachedEvent != null) {
                    Result.success(EventMapper.toModel(cachedEvent))
                } else {
                    Result.failure(Exception("Не удалось загрузить информацию о событии"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedEvent = eventDao.getEventById(id)
                if (cachedEvent != null) {
                    Result.success(EventMapper.toModel(cachedEvent))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
}


package com.example.zero_degree.core.storage.dao

import androidx.room.*
import com.example.zero_degree.core.storage.entity.EventEntity

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<EventEntity>
    
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?
    
    @Query("SELECT * FROM events WHERE barId = :barId")
    suspend fun getEventsByBarId(barId: String): List<EventEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
    
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
    
    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: String)
}


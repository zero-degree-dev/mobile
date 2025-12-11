package com.example.zero_degree.data.local.dao

import androidx.room.*
import com.example.zero_degree.data.local.entity.EventEntity

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<EventEntity>
    
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): EventEntity?
    
    @Query("SELECT * FROM events WHERE barId = :barId")
    suspend fun getEventsByBarId(barId: Int): List<EventEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
    
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
    
    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: Int)
}


package com.example.zero_degree.data.local.dao

import androidx.room.*
import com.example.zero_degree.data.local.entity.BarEntity

@Dao
interface BarDao {
    
    @Query("SELECT * FROM bars")
    suspend fun getAllBars(): List<BarEntity>
    
    @Query("SELECT * FROM bars WHERE id = :id")
    suspend fun getBarById(id: Int): BarEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBar(bar: BarEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBars(bars: List<BarEntity>)
    
    @Query("DELETE FROM bars")
    suspend fun deleteAllBars()
    
    @Query("DELETE FROM bars WHERE id = :id")
    suspend fun deleteBar(id: Int)
}


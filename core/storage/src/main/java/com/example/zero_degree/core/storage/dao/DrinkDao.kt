package com.example.zero_degree.core.storage.dao

import androidx.room.*
import com.example.zero_degree.core.storage.entity.DrinkEntity

@Dao
interface DrinkDao {
    
    @Query("SELECT * FROM drinks")
    suspend fun getAllDrinks(): List<DrinkEntity>
    
    @Query("SELECT * FROM drinks WHERE id = :id")
    suspend fun getDrinkById(id: String): DrinkEntity?
    
    @Query("SELECT * FROM drinks WHERE type = :type")
    suspend fun getDrinksByType(type: String): List<DrinkEntity>
    
    @Query("SELECT * FROM drinks WHERE available = 1")
    suspend fun getAvailableDrinks(): List<DrinkEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: DrinkEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinks: List<DrinkEntity>)
    
    @Query("DELETE FROM drinks")
    suspend fun deleteAllDrinks()
    
    @Query("DELETE FROM drinks WHERE id = :id")
    suspend fun deleteDrink(id: String)
}


package com.example.zero_degree.data.local.dao

import androidx.room.*
import com.example.zero_degree.data.local.entity.DrinkEntity

@Dao
interface DrinkDao {
    
    @Query("SELECT * FROM drinks")
    suspend fun getAllDrinks(): List<DrinkEntity>
    
    @Query("SELECT * FROM drinks WHERE type = :type")
    suspend fun getDrinksByType(type: String): List<DrinkEntity>
    
    @Query("SELECT * FROM drinks WHERE taste = :taste")
    suspend fun getDrinksByTaste(taste: String): List<DrinkEntity>
    
    @Query("SELECT * FROM drinks WHERE type = :type AND taste = :taste")
    suspend fun getDrinksByTypeAndTaste(type: String, taste: String): List<DrinkEntity>
    
    @Query("SELECT * FROM drinks WHERE id = :id")
    suspend fun getDrinkById(id: Int): DrinkEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: DrinkEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinks: List<DrinkEntity>)
    
    @Query("DELETE FROM drinks")
    suspend fun deleteAllDrinks()
    
    @Query("DELETE FROM drinks WHERE id = :id")
    suspend fun deleteDrink(id: Int)
}


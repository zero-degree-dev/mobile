package com.example.zero_degree.data.local.dao

import androidx.room.*
import com.example.zero_degree.data.local.entity.ReviewEntity

@Dao
interface ReviewDao {
    
    @Query("SELECT * FROM reviews WHERE drinkId = :drinkId")
    suspend fun getReviewsByDrinkId(drinkId: Int): List<ReviewEntity>
    
    @Query("SELECT * FROM reviews WHERE id = :id")
    suspend fun getReviewById(id: Int): ReviewEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)
    
    @Query("DELETE FROM reviews WHERE drinkId = :drinkId")
    suspend fun deleteReviewsByDrinkId(drinkId: Int)
    
    @Query("DELETE FROM reviews WHERE id = :id")
    suspend fun deleteReview(id: Int)
}


package com.example.zero_degree.core.storage.dao

import androidx.room.*
import com.example.zero_degree.core.storage.entity.ReviewEntity

@Dao
interface ReviewDao {
    
    @Query("SELECT * FROM reviews")
    suspend fun getAllReviews(): List<ReviewEntity>
    
    @Query("SELECT * FROM reviews WHERE id = :id")
    suspend fun getReviewById(id: String): ReviewEntity?
    
    @Query("SELECT * FROM reviews WHERE targetId = :targetId AND targetType = :targetType")
    suspend fun getReviewsByTarget(targetId: String, targetType: String): List<ReviewEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)
    
    @Query("DELETE FROM reviews")
    suspend fun deleteAllReviews()
    
    @Query("DELETE FROM reviews WHERE id = :id")
    suspend fun deleteReview(id: String)
}


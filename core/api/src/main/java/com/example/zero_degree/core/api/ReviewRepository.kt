package com.example.zero_degree.core.api

import android.content.Context
import android.util.Log
import com.example.zero_degree.core.api.model.CreateReviewRequest
import com.example.zero_degree.core.api.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReviewRepository(private val context: Context) {
    
    companion object {
        private const val TAG = "ReviewRepository"
    }
    
    private val apiService = RetrofitClient.getApiService(context)
    
    suspend fun getReviews(targetId: String, targetType: String): Result<List<Review>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getReviews: targetId=$targetId, targetType=$targetType")
            val response = apiService.getReviews(targetId, targetType)
            Log.d(TAG, "getReviews: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val reviews = response.body()!!
                Log.d(TAG, "getReviews: success, reviews count = ${reviews.size}")
                Result.success(reviews)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "getReviews: failed, error body = $errorBody")
                Result.failure(Exception("Не удалось загрузить отзывы: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getReviews: exception", e)
            Result.failure(e)
        }
    }
    
    suspend fun createReview(request: CreateReviewRequest): Result<Review> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "createReview: request = targetId=${request.targetId}, targetType=${request.targetType}, rating=${request.rating}")
            val response = apiService.createReview(request)
            Log.d(TAG, "createReview: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val review = response.body()!!
                Log.d(TAG, "createReview: success, review id = ${review.id}")
                Result.success(review)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "createReview: failed, error body = $errorBody")
                Result.failure(Exception("Не удалось создать отзыв: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "createReview: exception", e)
            Result.failure(e)
        }
    }
}


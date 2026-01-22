package com.example.zero_degree.core.api

import com.example.zero_degree.core.api.model.*
import retrofit2.Response
import retrofit2.http.*

// Интерфейс для API запросов
interface ApiService {
    
    // ========== Auth (Публичные) ==========
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    // ========== Users ==========
    
    @GET("users/me")
    suspend fun getCurrentUser(): Response<User>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>
    
    @PATCH("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>
    
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>
    
    // ========== Bars (Публичные) ==========
    
    @GET("bars")
    suspend fun getBars(
        @Query("name") name: String? = null,
        @Query("address") address: String? = null
    ): Response<List<Bar>>
    
    @GET("bars/{id}")
    suspend fun getBarById(@Path("id") id: String): Response<Bar>
    
    // ========== Drinks (Публичные) ==========
    
    @GET("drinks")
    suspend fun getDrinks(
        @Query("type") type: String? = null,
        @Query("name") name: String? = null,
        @Query("available") available: Boolean? = null
    ): Response<List<Drink>>
    
    @GET("drinks/{id}")
    suspend fun getDrinkById(@Path("id") id: String): Response<Drink>
    
    // ========== Events (Публичные) ==========
    
    @GET("events")
    suspend fun getEvents(
        @Query("barId") barId: String? = null,
        @Query("date") date: String? = null
    ): Response<List<Event>>
    
    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: String): Response<Event>
    
    // ========== Bookings ==========
    
    @GET("bookings")
    suspend fun getBookings(
        @Query("userId") userId: String? = null,
        @Query("barId") barId: String? = null,
        @Query("status") status: String? = null
    ): Response<List<Booking>>
    
    @GET("bookings/{id}")
    suspend fun getBookingById(@Path("id") id: String): Response<Booking>
    
    @POST("bookings")
    suspend fun createBooking(@Body request: CreateBookingRequest): Response<Booking>
    
    @PATCH("bookings/{id}")
    suspend fun updateBooking(@Path("id") id: String, @Body request: UpdateBookingRequest): Response<Booking>
    
    @DELETE("bookings/{id}")
    suspend fun cancelBooking(@Path("id") id: String): Response<Unit>
    
    // ========== Reviews ==========
    
    @GET("reviews")
    suspend fun getReviews(
        @Query("targetId") targetId: String? = null,
        @Query("targetType") targetType: String? = null
    ): Response<List<Review>>
    
    @POST("reviews")
    suspend fun createReview(@Body request: CreateReviewRequest): Response<Review>
    
    @PATCH("reviews/{id}")
    suspend fun updateReview(@Path("id") id: String, @Body request: UpdateReviewRequest): Response<Review>
    
    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String): Response<Unit>
}


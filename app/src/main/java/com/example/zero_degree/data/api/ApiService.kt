package com.example.zero_degree.data.api

import com.example.zero_degree.data.model.*
import retrofit2.Response
import retrofit2.http.*

// Интерфейс для API запросов
interface ApiService {
    
    // Авторизация
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    // Напитки
    @GET("drinks")
    suspend fun getDrinks(
        @Query("type") type: String? = null,
        @Query("taste") taste: String? = null
    ): Response<List<Drink>>
    
    @GET("drinks/{id}")
    suspend fun getDrinkById(@Path("id") id: Int): Response<Drink>
    
    // Бары
    @GET("bars")
    suspend fun getBars(): Response<List<Bar>>
    
    @GET("bars/{id}")
    suspend fun getBarById(@Path("id") id: Int): Response<Bar>
    
    // События
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>
    
    @POST("events/{id}/register")
    suspend fun registerForEvent(@Path("id") eventId: Int): Response<Unit>
    
    // Бронирования
    @GET("bookings")
    suspend fun getBookings(): Response<List<Booking>>
    
    @POST("bookings")
    suspend fun createBooking(@Body booking: Booking): Response<Booking>
    
    // Отзывы
    @GET("drinks/{id}/reviews")
    suspend fun getDrinkReviews(@Path("id") drinkId: Int): Response<List<Review>>
    
    @POST("drinks/{id}/reviews")
    suspend fun addReview(
        @Path("id") drinkId: Int,
        @Body review: Review
    ): Response<Review>
    
    // Пользователь
    @GET("user/profile")
    suspend fun getUserProfile(): Response<User>
}




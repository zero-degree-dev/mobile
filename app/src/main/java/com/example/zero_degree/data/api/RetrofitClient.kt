package com.example.zero_degree.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Класс для создания Retrofit клиента
object RetrofitClient {
    
    // Базовый URL API (замените на реальный)
    private const val BASE_URL = "https://api.zero-degree.com/api/"
    
    // Создаем OkHttpClient с логированием
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Создаем Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    // Получаем API сервис
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}




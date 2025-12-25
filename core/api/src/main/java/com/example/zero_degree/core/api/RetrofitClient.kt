package com.example.zero_degree.core.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Класс для создания Retrofit клиента
object RetrofitClient {
    
    // Базовый URL API
    // Для эмулятора Android используйте: http://10.0.2.2:3000/api
    // Для реального устройства используйте: http://localhost:3000/api или ваш IP
    private const val BASE_URL = "https://zero-degree.ru/api/"
    
    // Альтернативный URL для эмулятора (можно переключить)
    // private const val BASE_URL = "http://10.0.2.2:3000/api/"
    
    private var apiService: ApiService? = null
    
    // Получить API сервис с контекстом для AuthInterceptor
    fun getApiService(context: Context): ApiService {
        if (apiService == null) {
            // Создаем OkHttpClient с логированием и AuthInterceptor
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            
            // Создаем Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService!!
    }
    
    // Метод для сброса клиента (например, при смене токена)
    fun reset() {
        apiService = null
    }
}


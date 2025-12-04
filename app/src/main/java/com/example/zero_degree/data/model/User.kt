package com.example.zero_degree.data.model

// Модель пользователя
data class User(
    val id: Int,
    val email: String,
    val name: String,
    val age: Int,
    val zeroCoins: Int = 0 // бонусные монеты
)

// Модель для авторизации
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val age: Int
)

data class AuthResponse(
    val token: String,
    val user: User
)



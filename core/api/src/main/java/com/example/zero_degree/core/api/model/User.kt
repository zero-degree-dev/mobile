package com.example.zero_degree.core.api.model

// Модель пользователя
data class User(
    val id: String, // UUID
    val name: String,
    val email: String,
    val role: String, // "user" or "admin"
    val bonusBalance: Int = 0, // бонусные монеты
    val avatarUrl: String? = null // URL аватара пользователя
)

// Модель для авторизации
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val avatarUrl: String? = null
)

data class AuthResponse(
    val access_token: String,
    val user: User
)


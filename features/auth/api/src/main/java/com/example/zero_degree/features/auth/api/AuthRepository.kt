package com.example.zero_degree.features.auth.api

import com.example.zero_degree.core.api.model.AuthResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun register(name: String, email: String, password: String, avatarUrl: String? = null): Result<AuthResponse>
}


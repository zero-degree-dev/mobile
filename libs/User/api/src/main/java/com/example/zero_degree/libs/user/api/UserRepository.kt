package com.example.zero_degree.libs.user.api

import com.example.zero_degree.core.api.model.User

interface UserRepository {
    suspend fun getCurrentUser(): Result<User>
    suspend fun getUserById(id: String): Result<User>
    suspend fun updateUser(id: String, user: User): Result<User>
    suspend fun deleteUser(id: String): Result<Unit>
}


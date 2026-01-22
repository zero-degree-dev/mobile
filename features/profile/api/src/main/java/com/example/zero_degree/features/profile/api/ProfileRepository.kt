package com.example.zero_degree.features.profile.api

import com.example.zero_degree.core.api.model.User

interface ProfileRepository {
    suspend fun getUser(id: String): Result<User>
    suspend fun updateUser(id: String, user: User): Result<User>
    suspend fun deleteUser(id: String): Result<Unit>
}


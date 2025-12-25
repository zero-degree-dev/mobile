package com.example.zero_degree.features.home.api

import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.api.model.Event
import com.example.zero_degree.core.api.model.User

interface HomeRepository {
    suspend fun getCurrentUser(): Result<User>
    suspend fun getRecentBars(): Result<List<Bar>>
    suspend fun getRecentDrinks(): Result<List<Drink>>
    suspend fun getActiveEvents(): Result<List<Event>>
}


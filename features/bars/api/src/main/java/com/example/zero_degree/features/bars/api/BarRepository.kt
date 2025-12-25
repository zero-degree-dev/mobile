package com.example.zero_degree.features.bars.api

import com.example.zero_degree.core.api.model.Bar

interface BarRepository {
    suspend fun getBars(): Result<List<Bar>>
    suspend fun getBarById(id: String): Result<Bar>
}


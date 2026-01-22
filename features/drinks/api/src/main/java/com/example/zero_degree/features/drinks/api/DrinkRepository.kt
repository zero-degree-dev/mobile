package com.example.zero_degree.features.drinks.api

import com.example.zero_degree.core.api.model.Drink

interface DrinkRepository {
    suspend fun getDrinks(type: String? = null, name: String? = null, available: Boolean? = null): Result<List<Drink>>
    suspend fun getDrinkById(id: String): Result<Drink>
}


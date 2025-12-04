package com.example.zero_degree.data.model

// Модель бара
data class Bar(
    val id: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int, // вместимость
    val imageUrl: String,
    val description: String,
    val phone: String? = null
)



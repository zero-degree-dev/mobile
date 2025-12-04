package com.example.zero_degree.data.model

// Модель напитка
data class Drink(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val type: String, // тип напитка (пиво, лимонад и т.д.)
    val taste: String, // вкус (сладкий, горький и т.д.)
    val price: Double,
    val alcoholContent: Double = 0.0 // всегда 0 для безалкогольных
)



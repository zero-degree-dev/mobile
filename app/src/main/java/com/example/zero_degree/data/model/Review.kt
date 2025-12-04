package com.example.zero_degree.data.model

// Модель отзыва
data class Review(
    val id: Int,
    val drinkId: Int,
    val userId: Int,
    val userName: String,
    val rating: Int, // от 1 до 5
    val comment: String,
    val date: String
)



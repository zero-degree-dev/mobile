package com.example.zero_degree.core.api.model

// Модель отзыва
data class Review(
    val id: String, // UUID
    val targetId: String, // UUID - ID цели отзыва (drink или bar)
    val targetType: String, // тип цели: "drink" или "bar"
    val userId: String, // UUID
    val userName: String? = null,
    val rating: Int, // от 1 до 5
    val comment: String? = null,
    val date: String? = null // дата создания
)

// Request для создания отзыва
data class CreateReviewRequest(
    val targetId: String,
    val targetType: String, // "drink" или "bar"
    val rating: Int,
    val comment: String
)

// Request для обновления отзыва
data class UpdateReviewRequest(
    val rating: Int? = null,
    val comment: String? = null
)


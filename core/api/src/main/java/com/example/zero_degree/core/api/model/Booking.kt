package com.example.zero_degree.core.api.model

// Модель бронирования
data class Booking(
    val id: String, // UUID
    val barId: String, // UUID
    val userId: String, // UUID
    val date: String, // дата бронирования (формат: "2024-12-25")
    val time: String, // время бронирования (формат: "19:00:00")
    val guestsCount: Int, // количество гостей
    val status: String, // статус (pending, confirmed, cancelled, completed)
    val createdAt: String? = null, // дата создания (формат: "2025-12-25T00:49:19.791Z")
    val user: User? = null, // информация о пользователе
    val bar: Bar? = null // информация о баре
)

// Request для создания бронирования
data class CreateBookingRequest(
    val barId: String,
    val date: String,
    val time: String,
    val guestsCount: Int
)

// Request для обновления бронирования
data class UpdateBookingRequest(
    val date: String? = null,
    val time: String? = null,
    val guestsCount: Int? = null,
    val status: String? = null
)


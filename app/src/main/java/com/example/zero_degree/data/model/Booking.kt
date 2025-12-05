package com.example.zero_degree.data.model

// Модель бронирования
data class Booking(
    val id: Int,
    val barId: Int,
    val userId: Int,
    val date: String, // дата бронирования
    val time: String, // время бронирования
    val guestsCount: Int, // количество гостей
    val status: String // статус (pending, confirmed, cancelled)
)




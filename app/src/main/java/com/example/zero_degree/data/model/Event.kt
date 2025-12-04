package com.example.zero_degree.data.model

import java.util.Date

// Модель события
data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val date: String, // дата в формате строки
    val barId: Int,
    val imageUrl: String
)



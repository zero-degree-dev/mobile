package com.example.zero_degree.core.api.model

import com.google.gson.annotations.SerializedName

// Модель события
data class Event(
    val id: String, // UUID
    @SerializedName("title")
    val name: String, // API возвращает "title", но используем как "name"
    val description: String? = null,
    val date: String, // дата в формате строки
    @SerializedName("barId")
    val barId: String, // UUID
    @SerializedName("imageUrl")
    val imageUrl: String? = null
)


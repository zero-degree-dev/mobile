package com.example.zero_degree.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String, // UUID
    val name: String,
    val description: String? = null,
    val date: String,
    val barId: String, // UUID
    val imageUrl: String? = null
)


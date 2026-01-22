package com.example.zero_degree.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drinks")
data class DrinkEntity(
    @PrimaryKey
    val id: String, // UUID
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val type: String? = null,
    val taste: String? = null,
    val price: Double,
    val available: Boolean = true,
    val alcoholContent: Double = 0.0
)


package com.example.zero_degree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drinks")
data class DrinkEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val type: String,
    val taste: String,
    val price: Double,
    val alcoholContent: Double = 0.0
)


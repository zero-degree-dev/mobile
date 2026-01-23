package com.example.zero_degree.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bars")
data class BarEntity(
    @PrimaryKey
    val id: String, // UUID
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int,
    val imageUrl: String? = null,
    val description: String? = null,
    val phone: String? = null
)


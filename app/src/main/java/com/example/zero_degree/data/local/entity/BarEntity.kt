package com.example.zero_degree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bars")
data class BarEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int,
    val imageUrl: String,
    val description: String,
    val phone: String? = null
)


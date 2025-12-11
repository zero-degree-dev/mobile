package com.example.zero_degree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val date: String,
    val barId: Int,
    val imageUrl: String
)


package com.example.zero_degree.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey
    val id: String, // UUID
    val barId: String, // UUID
    val userId: String, // UUID
    val date: String,
    val time: String,
    val guestsCount: Int,
    val status: String // pending, confirmed, cancelled, completed
)


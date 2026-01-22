package com.example.zero_degree.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey
    val id: String, // UUID
    val targetId: String, // UUID
    val targetType: String, // "drink" or "bar"
    val userId: String, // UUID
    val userName: String? = null,
    val rating: Int,
    val comment: String? = null,
    val date: String? = null
)


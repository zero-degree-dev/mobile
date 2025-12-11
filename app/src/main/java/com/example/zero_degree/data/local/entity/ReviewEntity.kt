package com.example.zero_degree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey
    val id: Int,
    val drinkId: Int,
    val userId: Int,
    val userName: String,
    val rating: Int,
    val comment: String,
    val date: String
)


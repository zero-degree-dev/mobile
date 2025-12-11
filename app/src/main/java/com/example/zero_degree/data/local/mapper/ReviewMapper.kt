package com.example.zero_degree.data.local.mapper

import com.example.zero_degree.data.local.entity.ReviewEntity
import com.example.zero_degree.data.model.Review

object ReviewMapper {
    
    fun toEntity(review: Review): ReviewEntity {
        return ReviewEntity(
            id = review.id,
            drinkId = review.drinkId,
            userId = review.userId,
            userName = review.userName,
            rating = review.rating,
            comment = review.comment,
            date = review.date
        )
    }
    
    fun toModel(entity: ReviewEntity): Review {
        return Review(
            id = entity.id,
            drinkId = entity.drinkId,
            userId = entity.userId,
            userName = entity.userName,
            rating = entity.rating,
            comment = entity.comment,
            date = entity.date
        )
    }
    
    fun toModelList(entities: List<ReviewEntity>): List<Review> {
        return entities.map { toModel(it) }
    }
    
    fun toEntityList(reviews: List<Review>): List<ReviewEntity> {
        return reviews.map { toEntity(it) }
    }
}


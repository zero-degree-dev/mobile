package com.example.zero_degree.core.storage.mapper

import com.example.zero_degree.core.api.model.Review
import com.example.zero_degree.core.storage.entity.ReviewEntity

object ReviewMapper {
    
    fun toEntity(review: Review): ReviewEntity {
        return ReviewEntity(
            id = review.id,
            targetId = review.targetId,
            targetType = review.targetType,
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
            targetId = entity.targetId,
            targetType = entity.targetType,
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


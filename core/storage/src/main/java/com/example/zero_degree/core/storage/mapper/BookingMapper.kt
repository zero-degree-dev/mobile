package com.example.zero_degree.core.storage.mapper

import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.storage.entity.BookingEntity

object BookingMapper {
    
    fun toEntity(booking: Booking): BookingEntity {
        return BookingEntity(
            id = booking.id,
            barId = booking.barId,
            userId = booking.userId,
            date = booking.date,
            time = booking.time,
            guestsCount = booking.guestsCount,
            status = booking.status
        )
    }
    
    fun toModel(entity: BookingEntity): Booking {
        return Booking(
            id = entity.id,
            barId = entity.barId,
            userId = entity.userId,
            date = entity.date,
            time = entity.time,
            guestsCount = entity.guestsCount,
            status = entity.status
        )
    }
    
    fun toModelList(entities: List<BookingEntity>): List<Booking> {
        return entities.map { toModel(it) }
    }
    
    fun toEntityList(bookings: List<Booking>): List<BookingEntity> {
        return bookings.map { toEntity(it) }
    }
}


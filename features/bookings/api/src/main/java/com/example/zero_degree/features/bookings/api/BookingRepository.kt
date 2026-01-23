package com.example.zero_degree.features.bookings.api

import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.api.model.CreateBookingRequest
import com.example.zero_degree.core.api.model.UpdateBookingRequest

interface BookingRepository {
    suspend fun getBookings(userId: String? = null, barId: String? = null, status: String? = null): Result<List<Booking>>
    suspend fun getBookingById(id: String): Result<Booking>
    suspend fun createBooking(request: CreateBookingRequest): Result<Booking>
    suspend fun updateBooking(id: String, request: UpdateBookingRequest): Result<Booking>
    suspend fun cancelBooking(id: String): Result<Unit>
}


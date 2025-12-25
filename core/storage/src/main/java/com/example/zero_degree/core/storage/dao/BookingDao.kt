package com.example.zero_degree.core.storage.dao

import androidx.room.*
import com.example.zero_degree.core.storage.entity.BookingEntity

@Dao
interface BookingDao {
    
    @Query("SELECT * FROM bookings")
    suspend fun getAllBookings(): List<BookingEntity>
    
    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: String): BookingEntity?
    
    @Query("SELECT * FROM bookings WHERE userId = :userId")
    suspend fun getBookingsByUserId(userId: String): List<BookingEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)
    
    @Query("DELETE FROM bookings")
    suspend fun deleteAllBookings()
    
    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBooking(id: String)
}


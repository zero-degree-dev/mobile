package com.example.zero_degree.features.bookings.impl

import android.content.Context
import android.util.Log
import com.example.zero_degree.core.api.RetrofitClient
import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.api.model.CreateBookingRequest
import com.example.zero_degree.core.api.model.UpdateBookingRequest
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.api.TokenManager
import com.example.zero_degree.core.storage.mapper.BookingMapper
import com.example.zero_degree.features.bookings.api.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingRepositoryImpl(private val context: Context) : BookingRepository {
    
    companion object {
        private const val TAG = "BookingRepositoryImpl"
    }
    
    private val apiService = RetrofitClient.getApiService(context)
    private val database = AppDatabase.getDatabase(context)
    private val bookingDao = database.bookingDao()
    
    override suspend fun getBookings(userId: String?, barId: String?, status: String?): Result<List<Booking>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBookings(userId, barId, status)
            if (response.isSuccessful && response.body() != null) {
                val bookings = response.body()!!
                bookingDao.insertBookings(BookingMapper.toEntityList(bookings))
                Result.success(bookings)
            } else {
                val cachedBookings = if (userId != null) {
                    bookingDao.getBookingsByUserId(userId)
                } else {
                    bookingDao.getAllBookings()
                }
                if (cachedBookings.isNotEmpty()) {
                    Result.success(BookingMapper.toModelList(cachedBookings))
                } else {
                    Result.failure(Exception("Не удалось загрузить список бронирований"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedBookings = if (userId != null) {
                    bookingDao.getBookingsByUserId(userId)
                } else {
                    bookingDao.getAllBookings()
                }
                if (cachedBookings.isNotEmpty()) {
                    Result.success(BookingMapper.toModelList(cachedBookings))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getBookingById(id: String): Result<Booking> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBookingById(id)
            if (response.isSuccessful && response.body() != null) {
                val booking = response.body()!!
                bookingDao.insertBooking(BookingMapper.toEntity(booking))
                Result.success(booking)
            } else {
                val cachedBooking = bookingDao.getBookingById(id)
                if (cachedBooking != null) {
                    Result.success(BookingMapper.toModel(cachedBooking))
                } else {
                    Result.failure(Exception("Не удалось загрузить информацию о бронировании"))
                }
            }
        } catch (e: Exception) {
            try {
                val cachedBooking = bookingDao.getBookingById(id)
                if (cachedBooking != null) {
                    Result.success(BookingMapper.toModel(cachedBooking))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun createBooking(request: CreateBookingRequest): Result<Booking> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "createBooking: request = barId=${request.barId}, date=${request.date}, time=${request.time}, guestsCount=${request.guestsCount}")
            val response = apiService.createBooking(request)
            Log.d(TAG, "createBooking: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val booking = response.body()!!
                Log.d(TAG, "createBooking: success, booking id = ${booking.id}")
                bookingDao.insertBooking(BookingMapper.toEntity(booking))
                Result.success(booking)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "createBooking: failed, error body = $errorBody")
                Result.failure(Exception("Не удалось создать бронирование: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "createBooking: exception", e)
            Result.failure(e)
        }
    }
    
    override suspend fun updateBooking(id: String, request: UpdateBookingRequest): Result<Booking> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "updateBooking: id=$id, request = date=${request.date}, time=${request.time}, guestsCount=${request.guestsCount}")
            val response = apiService.updateBooking(id, request)
            Log.d(TAG, "updateBooking: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val booking = response.body()!!
                Log.d(TAG, "updateBooking: success, booking id = ${booking.id}")
                bookingDao.insertBooking(BookingMapper.toEntity(booking))
                Result.success(booking)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "updateBooking: failed, error body = $errorBody")
                Result.failure(Exception("Не удалось обновить бронирование: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateBooking: exception", e)
            Result.failure(e)
        }
    }
    
    override suspend fun cancelBooking(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.cancelBooking(id)
            if (response.isSuccessful) {
                bookingDao.deleteBooking(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Не удалось отменить бронирование"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


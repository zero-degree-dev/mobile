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

    /**
     * Быстрое чтение из персистентного хранилища (Room) без ожидания сети.
     * Нужен для сценария "cache-first, then refresh".
     */
    suspend fun getCachedBookings(userId: String? = null): List<Booking> = withContext(Dispatchers.IO) {
        val cached = if (userId != null) {
            bookingDao.getBookingsByUserId(userId)
        } else {
            bookingDao.getAllBookings()
        }
        BookingMapper.toModelList(cached)
    }
    
    override suspend fun getBookings(userId: String?, barId: String?, status: String?): Result<List<Booking>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getBookings: Запрос к API (userId=$userId, barId=$barId, status=$status)")
            val response = apiService.getBookings(userId, barId, status)
            Log.d(TAG, "getBookings: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val bookings = response.body()!!
                Log.d(TAG, "getBookings: Получено ${bookings.size} бронирований из API")
                
                try {
                    val entities = BookingMapper.toEntityList(bookings)
                    Log.d(TAG, "getBookings: Конвертировано ${entities.size} entities для сохранения")
                    bookingDao.insertBookings(entities)
                    Log.d(TAG, "getBookings: Данные успешно сохранены в БД")
                    
                    val savedCount = bookingDao.getAllBookings().size
                    Log.d(TAG, "getBookings: Проверка БД - сохранено $savedCount бронирований")
                } catch (dbException: Exception) {
                    Log.e(TAG, "getBookings: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(bookings)
            } else {
                Log.w(TAG, "getBookings: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedBookings = if (userId != null) {
                    bookingDao.getBookingsByUserId(userId)
                } else {
                    bookingDao.getAllBookings()
                }
                Log.d(TAG, "getBookings: Найдено ${cachedBookings.size} бронирований в кэше")
                if (cachedBookings.isNotEmpty()) {
                    Result.success(BookingMapper.toModelList(cachedBookings))
                } else {
                    Result.failure(Exception("Не удалось загрузить список бронирований"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getBookings: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getBookings: Пытаемся загрузить из кэша после ошибки")
                val cachedBookings = if (userId != null) {
                    bookingDao.getBookingsByUserId(userId)
                } else {
                    bookingDao.getAllBookings()
                }
                Log.d(TAG, "getBookings: Найдено ${cachedBookings.size} бронирований в кэше")
                if (cachedBookings.isNotEmpty()) {
                    Result.success(BookingMapper.toModelList(cachedBookings))
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getBookings: Ошибка при чтении из БД", dbException)
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getBookingById(id: String): Result<Booking> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getBookingById: Запрос к API для бронирования id=$id")
            val response = apiService.getBookingById(id)
            Log.d(TAG, "getBookingById: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful && response.body() != null) {
                val booking = response.body()!!
                Log.d(TAG, "getBookingById: Получено бронирование из API: id=${booking.id}, barId=${booking.barId}")
                
                try {
                    val entity = BookingMapper.toEntity(booking)
                    Log.d(TAG, "getBookingById: Конвертировано в entity: id=${entity.id}")
                    bookingDao.insertBooking(entity)
                    Log.d(TAG, "getBookingById: Бронирование успешно сохранено в БД")
                    
                    val savedBooking = bookingDao.getBookingById(id)
                    if (savedBooking != null) {
                        Log.d(TAG, "getBookingById: Проверка БД - бронирование найдено: id=${savedBooking.id}")
                    } else {
                        Log.w(TAG, "getBookingById: ВНИМАНИЕ! Бронирование не найдено в БД после сохранения!")
                    }
                } catch (dbException: Exception) {
                    Log.e(TAG, "getBookingById: Ошибка при сохранении в БД", dbException)
                }
                
                Result.success(booking)
            } else {
                Log.w(TAG, "getBookingById: API запрос неуспешен, пытаемся загрузить из кэша")
                val cachedBooking = bookingDao.getBookingById(id)
                if (cachedBooking != null) {
                    Log.d(TAG, "getBookingById: Найдено бронирование в кэше: id=${cachedBooking.id}")
                    Result.success(BookingMapper.toModel(cachedBooking))
                } else {
                    Log.w(TAG, "getBookingById: Бронирование не найдено в кэше")
                    Result.failure(Exception("Не удалось загрузить информацию о бронировании"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getBookingById: Исключение при запросе к API", e)
            try {
                Log.d(TAG, "getBookingById: Пытаемся загрузить из кэша после ошибки")
                val cachedBooking = bookingDao.getBookingById(id)
                if (cachedBooking != null) {
                    Log.d(TAG, "getBookingById: Найдено бронирование в кэше: id=${cachedBooking.id}")
                    Result.success(BookingMapper.toModel(cachedBooking))
                } else {
                    Log.w(TAG, "getBookingById: Бронирование не найдено в кэше")
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Log.e(TAG, "getBookingById: Ошибка при чтении из БД", dbException)
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
                
                try {
                    val entity = BookingMapper.toEntity(booking)
                    Log.d(TAG, "createBooking: Конвертировано в entity: id=${entity.id}")
                    bookingDao.insertBooking(entity)
                    Log.d(TAG, "createBooking: Бронирование успешно сохранено в БД")
                    
                    val savedBooking = bookingDao.getBookingById(booking.id)
                    if (savedBooking != null) {
                        Log.d(TAG, "createBooking: Проверка БД - бронирование найдено: id=${savedBooking.id}")
                    } else {
                        Log.w(TAG, "createBooking: ВНИМАНИЕ! Бронирование не найдено в БД после сохранения!")
                    }
                } catch (dbException: Exception) {
                    Log.e(TAG, "createBooking: Ошибка при сохранении в БД", dbException)
                }
                
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
                
                try {
                    val entity = BookingMapper.toEntity(booking)
                    Log.d(TAG, "updateBooking: Конвертировано в entity: id=${entity.id}")
                    bookingDao.insertBooking(entity)
                    Log.d(TAG, "updateBooking: Бронирование успешно сохранено в БД")
                    
                    val savedBooking = bookingDao.getBookingById(booking.id)
                    if (savedBooking != null) {
                        Log.d(TAG, "updateBooking: Проверка БД - бронирование найдено: id=${savedBooking.id}")
                    } else {
                        Log.w(TAG, "updateBooking: ВНИМАНИЕ! Бронирование не найдено в БД после сохранения!")
                    }
                } catch (dbException: Exception) {
                    Log.e(TAG, "updateBooking: Ошибка при сохранении в БД", dbException)
                }
                
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
            Log.d(TAG, "cancelBooking: Запрос к API для отмены бронирования id=$id")
            val response = apiService.cancelBooking(id)
            Log.d(TAG, "cancelBooking: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                try {
                    bookingDao.deleteBooking(id)
                    Log.d(TAG, "cancelBooking: Бронирование успешно удалено из БД")
                    
                    val deletedBooking = bookingDao.getBookingById(id)
                    if (deletedBooking == null) {
                        Log.d(TAG, "cancelBooking: Проверка БД - бронирование удалено")
                    } else {
                        Log.w(TAG, "cancelBooking: ВНИМАНИЕ! Бронирование все еще найдено в БД после удаления!")
                    }
                } catch (dbException: Exception) {
                    Log.e(TAG, "cancelBooking: Ошибка при удалении из БД", dbException)
                }
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "cancelBooking: failed, error body = $errorBody")
                Result.failure(Exception("Не удалось отменить бронирование"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "cancelBooking: Исключение", e)
            Result.failure(e)
        }
    }
}


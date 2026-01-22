package com.example.zero_degree.features.bookings.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.api.TokenManager
import com.example.zero_degree.core.storage.AppDatabase
import com.example.zero_degree.core.storage.mapper.BookingMapper
import com.example.zero_degree.features.bookings.api.BookingRepository
import com.example.zero_degree.features.bookings.impl.BookingRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class BookingListViewModel(application: Application) : AndroidViewModel(application) {
    
    private val context = application.applicationContext
    private val repository: BookingRepository = BookingRepositoryImpl(context)
    
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings = _bookings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private var allBookings: List<Booking> = emptyList()
    private var isShowingCurrent = true
    
    fun loadBookings() {
        viewModelScope.launch {
            val userId = TokenManager.getUserId(context)
            if (userId == null) return@launch

            // 1) Сразу показываем данные из персистентного хранилища (если есть),
            // чтобы не ждать сетевого таймаута.
            try {
                val cachedBookings = withContext(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(context).bookingDao()
                    BookingMapper.toModelList(dao.getBookingsByUserId(userId))
                }
                if (cachedBookings.isNotEmpty()) {
                    allBookings = cachedBookings
                    filterBookings()
                }
            } catch (_: Exception) {
                // Если кеш недоступен, просто продолжаем с сетевым запросом
            }

            // 2) Обновляем данные с сервера (и репозиторий сохранит их в БД)
            _isLoading.value = true
            repository.getBookings(userId, null, null).fold(
                onSuccess = { bookingsList ->
                    allBookings = bookingsList
                    filterBookings()
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
    
    fun showCurrentBookings() {
        isShowingCurrent = true
        filterBookings()
    }
    
    fun showHistoryBookings() {
        isShowingCurrent = false
        filterBookings()
    }
    
    private fun filterBookings() {
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(now)
        
        val filtered = if (isShowingCurrent) {
            allBookings.filter { booking ->
                booking.date >= currentDate && booking.status != "cancelled"
            }
        } else {
            allBookings.filter { booking ->
                booking.date < currentDate || booking.status == "cancelled"
            }
        }
        
        _bookings.value = filtered.sortedByDescending { it.date }
    }
    
    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.cancelBooking(bookingId).fold(
                onSuccess = {
                    loadBookings() // Перезагружаем бронирования после отмены
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
}


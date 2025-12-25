package com.example.zero_degree.features.home.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.TokenManager
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.api.model.Event
import com.example.zero_degree.core.api.model.User
import com.example.zero_degree.features.bookings.api.BookingRepository
import com.example.zero_degree.features.bookings.impl.BookingRepositoryImpl
import com.example.zero_degree.features.home.api.HomeRepository
import com.example.zero_degree.features.home.impl.HomeRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val context = application.applicationContext
    private val repository: HomeRepository = HomeRepositoryImpl(context)
    
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()
    
    private val _recentBars = MutableStateFlow<List<Bar>>(emptyList())
    val recentBars = _recentBars.asStateFlow()
    
    private val _recentDrinks = MutableStateFlow<List<Drink>>(emptyList())
    val recentDrinks = _recentDrinks.asStateFlow()
    
    private val _activeEvents = MutableStateFlow<List<Event>>(emptyList())
    val activeEvents = _activeEvents.asStateFlow()
    
    private val _upcomingBookings = MutableStateFlow<List<Booking>>(emptyList())
    val upcomingBookings = _upcomingBookings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _bookingCancelled = MutableStateFlow(false)
    val bookingCancelled = _bookingCancelled.asStateFlow()
    
    private val bookingRepository: BookingRepository = BookingRepositoryImpl(context)
    
    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Загружаем профиль пользователя
                repository.getCurrentUser().fold(
                    onSuccess = { userData ->
                        _user.value = userData
                    },
                    onFailure = { e ->
                        // Логируем ошибку, но не падаем
                        e.printStackTrace()
                    }
                )
                
                // Загружаем бары (берем первые 3)
                repository.getRecentBars().fold(
                    onSuccess = { bars ->
                        _recentBars.value = bars.take(3)
                    },
                    onFailure = { e ->
                        e.printStackTrace()
                    }
                )
                
                // Загружаем напитки (берем первые 3)
                repository.getRecentDrinks().fold(
                    onSuccess = { drinks ->
                        _recentDrinks.value = drinks.take(3)
                    },
                    onFailure = { e ->
                        e.printStackTrace()
                    }
                )
                
                // Загружаем события (берем первые 3)
                repository.getActiveEvents().fold(
                    onSuccess = { events ->
                        _activeEvents.value = events.take(3)
                    },
                    onFailure = { e ->
                        e.printStackTrace()
                    }
                )
                
                // Загружаем бронирования пользователя
                val userId = TokenManager.getUserId(context)
                userId?.let {
                    bookingRepository.getBookings(it, null, null).fold(
                        onSuccess = { bookings ->
                            // Фильтруем только активные (не отмененные) и сортируем от ближайшей к дальнейшей
                            val now = Date()
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                            val currentDate = dateFormat.format(now)
                            val currentTime = timeFormat.format(now)
                            
                            val activeBookings = bookings.filter { booking ->
                                booking.status != "cancelled" && 
                                (booking.date > currentDate || 
                                 (booking.date == currentDate && booking.time >= currentTime))
                            }
                            
                            // Сортируем от ближайшей к дальнейшей (по дате и времени)
                            val sortedBookings = activeBookings.sortedWith(compareBy<Booking> { booking ->
                                // Создаем комбинированную строку для сортировки
                                "${booking.date} ${booking.time}"
                            })
                            
                            _upcomingBookings.value = sortedBookings
                        },
                        onFailure = { e ->
                            e.printStackTrace()
                        }
                    )
                }
            } catch (e: Exception) {
                // В случае ошибки оставляем пустые списки
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            bookingRepository.cancelBooking(bookingId).fold(
                onSuccess = {
                    _bookingCancelled.value = true
                },
                onFailure = { e ->
                    e.printStackTrace()
                }
            )
        }
    }
    
    fun resetBookingCancelled() {
        _bookingCancelled.value = false
    }
}


package com.example.zero_degree.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.data.model.Bar
import com.example.zero_degree.data.model.Booking
import com.example.zero_degree.data.repository.BarRepository
import com.example.zero_degree.data.repository.BookingRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

// ViewModel для бронирования
class BookingViewModel : ViewModel() {
    
    private val barRepository = BarRepository()
    private val bookingRepository = BookingRepository()
    
    // Список баров для выбора
    private val _bars = MutableLiveData<List<Bar>>()
    val bars: LiveData<List<Bar>> = _bars
    
    // Выбранный бар
    private val _selectedBar = MutableLiveData<Bar?>()
    val selectedBar: LiveData<Bar?> = _selectedBar
    
    // Выбранная дата
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate
    
    // Выбранное время
    private val _selectedTime = MutableLiveData<String>()
    val selectedTime: LiveData<String> = _selectedTime
    
    // Количество гостей
    private val _guestsCount = MutableLiveData<Int>()
    val guestsCount: LiveData<Int> = _guestsCount
    
    // Состояние загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Состояние успешного бронирования
    private val _bookingSuccess = MutableLiveData<Boolean>()
    val bookingSuccess: LiveData<Boolean> = _bookingSuccess
    
    init {
        _selectedDate.value = ""
        _selectedTime.value = ""
        _guestsCount.value = 1
    }
    
    // Загрузить бары
    fun loadBars() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val barsList = barRepository.getBars()
                _bars.value = barsList
            } catch (e: Exception) {
                // В случае ошибки
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Установить выбранный бар
    fun setSelectedBar(bar: Bar) {
        _selectedBar.value = bar
    }
    
    // Установить дату
    fun setDate(date: String) {
        _selectedDate.value = date
    }
    
    // Установить время
    fun setTime(time: String) {
        _selectedTime.value = time
    }
    
    // Установить количество гостей
    fun setGuestsCount(count: Int) {
        _guestsCount.value = count
    }
    
    // Создать бронирование
    fun createBooking(userId: Int) {
        viewModelScope.launch {
            val bar = _selectedBar.value ?: return@launch
            val date = _selectedDate.value ?: return@launch
            val time = _selectedTime.value ?: return@launch
            
            if (date.isEmpty() || time.isEmpty()) {
                return@launch
            }
            
            _isLoading.value = true
            try {
                val booking = Booking(
                    id = 0,
                    barId = bar.id,
                    userId = userId,
                    date = date,
                    time = time,
                    guestsCount = _guestsCount.value ?: 1,
                    status = "pending"
                )
                
                val result = bookingRepository.createBooking(booking)
                _bookingSuccess.value = result != null
            } catch (e: Exception) {
                _bookingSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Сбросить состояние успешного бронирования
    fun resetBookingSuccess() {
        _bookingSuccess.value = false
    }
}


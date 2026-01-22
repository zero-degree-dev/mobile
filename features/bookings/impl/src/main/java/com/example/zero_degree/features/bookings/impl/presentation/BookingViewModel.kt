package com.example.zero_degree.features.bookings.impl.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.api.model.CreateBookingRequest
import com.example.zero_degree.core.api.model.UpdateBookingRequest
import com.example.zero_degree.features.bars.api.BarRepository
import com.example.zero_degree.features.bookings.api.BookingRepository
import com.example.zero_degree.features.bookings.impl.BarRepositoryImpl
import com.example.zero_degree.features.bookings.impl.BookingRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(application: Application) : AndroidViewModel(application) {
    
    companion object {
        private const val TAG = "BookingViewModel"
    }
    
    private val barRepository: BarRepository = BarRepositoryImpl(application.applicationContext)
    private val bookingRepository: BookingRepository = BookingRepositoryImpl(application.applicationContext)
    
    private val _bars = MutableStateFlow<List<Bar>>(emptyList())
    val bars = _bars.asStateFlow()
    
    private val _selectedBar = MutableStateFlow<Bar?>(null)
    val selectedBar = _selectedBar.asStateFlow()
    
    private val _selectedDate = MutableStateFlow("")
    val selectedDate = _selectedDate.asStateFlow()
    
    private val _selectedTime = MutableStateFlow("")
    val selectedTime = _selectedTime.asStateFlow()
    
    private val _guestsCount = MutableStateFlow(1)
    val guestsCount = _guestsCount.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _bookingSuccess = MutableStateFlow(false)
    val bookingSuccess = _bookingSuccess.asStateFlow()
    
    private var editingBookingId: String? = null
    
    fun setEditingBookingId(bookingId: String?) {
        editingBookingId = bookingId
    }
    
    fun loadBars() {
        viewModelScope.launch {
            _isLoading.value = true
            barRepository.getBars().fold(
                onSuccess = { barsList ->
                    _bars.value = barsList
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
    
    fun setSelectedBar(bar: Bar) {
        _selectedBar.value = bar
    }
    
    fun setDate(date: String) {
        _selectedDate.value = date
    }
    
    fun setTime(time: String) {
        _selectedTime.value = time
    }
    
    fun setGuestsCount(count: Int) {
        _guestsCount.value = count
    }
    
    fun createBooking() {
        val bookingId = editingBookingId
        
        if (bookingId != null) {
            // Режим редактирования
            updateBooking(bookingId)
        } else {
            // Режим создания
            val barId = _selectedBar.value?.id ?: run {
                Log.e(TAG, "createBooking: barId is null")
                return
            }
            val date = _selectedDate.value
            val time = _selectedTime.value
            val guestsCount = _guestsCount.value
            
            Log.d(TAG, "createBooking called with:")
            Log.d(TAG, "  barId: $barId")
            Log.d(TAG, "  date: $date")
            Log.d(TAG, "  time: $time")
            Log.d(TAG, "  guestsCount: $guestsCount")
            
            if (barId.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Log.e(TAG, "createBooking: validation failed - barId.isEmpty: ${barId.isEmpty()}, date.isEmpty: ${date.isEmpty()}, time.isEmpty: ${time.isEmpty()}")
                return
            }
            
            viewModelScope.launch {
                _isLoading.value = true
                val request = CreateBookingRequest(barId, date, time, guestsCount)
                Log.d(TAG, "Sending booking request: barId=$barId, date=$date, time=$time, guestsCount=$guestsCount")
                
                bookingRepository.createBooking(request).fold(
                    onSuccess = { booking ->
                        Log.d(TAG, "Booking created successfully: ${booking.id}")
                        _bookingSuccess.value = true
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Failed to create booking", error)
                    }
                )
                _isLoading.value = false
            }
        }
    }
    
    private fun updateBooking(bookingId: String) {
        val date = _selectedDate.value
        val time = _selectedTime.value
        val guestsCount = _guestsCount.value
        
        Log.d(TAG, "updateBooking called with:")
        Log.d(TAG, "  bookingId: $bookingId")
        Log.d(TAG, "  date: $date")
        Log.d(TAG, "  time: $time")
        Log.d(TAG, "  guestsCount: $guestsCount")
        
        if (date.isEmpty() || time.isEmpty()) {
            Log.e(TAG, "updateBooking: validation failed - date.isEmpty: ${date.isEmpty()}, time.isEmpty: ${time.isEmpty()}")
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            val request = UpdateBookingRequest(date = date, time = time, guestsCount = guestsCount)
            Log.d(TAG, "Sending update request: bookingId=$bookingId, date=$date, time=$time, guestsCount=$guestsCount")
            
            bookingRepository.updateBooking(bookingId, request).fold(
                onSuccess = { booking ->
                    Log.d(TAG, "Booking updated successfully: ${booking.id}")
                    _bookingSuccess.value = true
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to update booking", error)
                }
            )
            _isLoading.value = false
        }
    }
    
    fun resetBookingSuccess() {
        _bookingSuccess.value = false
    }
}


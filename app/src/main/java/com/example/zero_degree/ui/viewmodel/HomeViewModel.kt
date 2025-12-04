package com.example.zero_degree.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.data.model.Bar
import com.example.zero_degree.data.model.Event
import com.example.zero_degree.data.repository.BarRepository
import com.example.zero_degree.data.repository.EventRepository
import kotlinx.coroutines.launch

// ViewModel для главного экрана
class HomeViewModel : ViewModel() {
    
    private val barRepository = BarRepository()
    private val eventRepository = EventRepository()
    
    // Состояние для последних баров
    private val _recentBars = MutableLiveData<List<Bar>>()
    val recentBars: LiveData<List<Bar>> = _recentBars
    
    // Состояние для активных событий
    private val _activeEvents = MutableLiveData<List<Event>>()
    val activeEvents: LiveData<List<Event>> = _activeEvents
    
    // Состояние загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Загрузить данные для главного экрана
    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Загружаем бары (берем первые 3 как последние посещенные)
                val bars = barRepository.getBars()
                _recentBars.value = bars.take(3)
                
                // Загружаем события
                val events = eventRepository.getEvents()
                _activeEvents.value = events
            } catch (e: Exception) {
                // В случае ошибки оставляем пустые списки
            } finally {
                _isLoading.value = false
            }
        }
    }
}


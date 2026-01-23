package com.example.zero_degree.features.events.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.model.Event
import com.example.zero_degree.features.events.api.EventRepository
import com.example.zero_degree.features.events.impl.EventRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val context = application.applicationContext
    private val repository: EventRepository = EventRepositoryImpl(context)
    
    private val _event = MutableStateFlow<Event?>(null)
    val event = _event.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getEventById(eventId).fold(
                onSuccess = { event ->
                    _event.value = event
                },
                onFailure = { exception ->
                    _error.value = "Ошибка загрузки события: ${exception.message}"
                }
            )
            _isLoading.value = false
        }
    }
}


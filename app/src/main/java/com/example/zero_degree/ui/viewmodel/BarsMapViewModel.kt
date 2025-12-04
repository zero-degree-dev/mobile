package com.example.zero_degree.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.data.model.Bar
import com.example.zero_degree.data.repository.BarRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

// ViewModel для карты баров
class BarsMapViewModel : ViewModel() {
    
    private val barRepository = BarRepository()
    
    // Список баров
    private val _bars = MutableLiveData<List<Bar>>()
    val bars: LiveData<List<Bar>> = _bars
    
    // Выбранный бар
    private val _selectedBar = MutableLiveData<Bar?>()
    val selectedBar: LiveData<Bar?> = _selectedBar
    
    // Состояние загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
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
    
    // Выбрать бар
    fun selectBar(bar: Bar) {
        _selectedBar.value = bar
    }
    
    // Сбросить выбор
    fun clearSelection() {
        _selectedBar.value = null
    }
}


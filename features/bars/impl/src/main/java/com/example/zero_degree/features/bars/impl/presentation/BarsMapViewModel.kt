package com.example.zero_degree.features.bars.impl.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.features.bars.api.BarRepository
import com.example.zero_degree.features.bars.impl.BarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BarsMapViewModel(application: Application) : AndroidViewModel(application) {
    
    companion object {
        private const val TAG = "BarsMapViewModel"
    }
    
    private val repository: BarRepository = BarRepositoryImpl(application.applicationContext)
    
    private val _bars = MutableStateFlow<List<Bar>>(emptyList())
    val bars = _bars.asStateFlow()
    
    private val _selectedBar = MutableStateFlow<Bar?>(null)
    val selectedBar = _selectedBar.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    fun loadBars() {
        Log.d(TAG, "loadBars вызван")
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Начинаем загрузку баров...")
            repository.getBars().fold(
                onSuccess = { barsList ->
                    Log.d(TAG, "Бары загружены успешно, количество: ${barsList.size}")
                    barsList.forEach { bar ->
                        Log.d(TAG, "Бар: ${bar.name}, координаты: (${bar.latitude}, ${bar.longitude})")
                    }
                    _bars.value = barsList
                },
                onFailure = { error ->
                    Log.e(TAG, "Ошибка при загрузке баров", error)
                }
            )
            _isLoading.value = false
            Log.d(TAG, "Загрузка баров завершена")
        }
    }
    
    fun selectBar(bar: Bar) {
        _selectedBar.value = bar
    }
}


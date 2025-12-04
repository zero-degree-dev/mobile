package com.example.zero_degree.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.data.model.Drink
import com.example.zero_degree.data.repository.DrinkRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

// ViewModel для каталога напитков
class DrinkCatalogViewModel : ViewModel() {
    
    private val drinkRepository = DrinkRepository()
    
    // Список напитков
    private val _drinks = MutableLiveData<List<Drink>>()
    val drinks: LiveData<List<Drink>> = _drinks
    
    // Выбранный фильтр по типу
    private val _selectedType = MutableLiveData<String?>()
    val selectedType: LiveData<String?> = _selectedType
    
    // Выбранный фильтр по вкусу
    private val _selectedTaste = MutableLiveData<String?>()
    val selectedTaste: LiveData<String?> = _selectedTaste
    
    // Состояние загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Загрузить напитки
    fun loadDrinks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val drinksList = drinkRepository.getDrinks(
                    type = _selectedType.value,
                    taste = _selectedTaste.value
                )
                _drinks.value = drinksList
            } catch (e: Exception) {
                // В случае ошибки оставляем пустой список
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Установить фильтр по типу
    fun setTypeFilter(type: String?) {
        _selectedType.value = type
        loadDrinks()
    }
    
    // Установить фильтр по вкусу
    fun setTasteFilter(taste: String?) {
        _selectedTaste.value = taste
        loadDrinks()
    }
    
    init {
        _selectedType.value = null
        _selectedTaste.value = null
    }
}


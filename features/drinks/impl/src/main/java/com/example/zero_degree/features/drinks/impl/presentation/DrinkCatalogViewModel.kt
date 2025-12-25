package com.example.zero_degree.features.drinks.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.features.drinks.api.DrinkRepository
import com.example.zero_degree.features.drinks.impl.DrinkRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DrinkCatalogViewModel(application: Application) : AndroidViewModel(application) {
    
    private val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application.applicationContext)
    
    private val _drinks = MutableStateFlow<List<Drink>>(emptyList())
    val drinks = _drinks.asStateFlow()
    
    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType = _selectedType.asStateFlow()
    
    private val _selectedTaste = MutableStateFlow<String?>(null)
    val selectedTaste = _selectedTaste.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    fun loadDrinks() {
        viewModelScope.launch {
            _isLoading.value = true
            drinkRepository.getDrinks(
                type = _selectedType.value,
                name = null,
                available = true
            ).fold(
                onSuccess = { drinksList ->
                    // Фильтруем по вкусу на клиенте, если нужно
                    val filtered = if (_selectedTaste.value != null) {
                        drinksList.filter { it.taste == _selectedTaste.value }
                    } else {
                        drinksList
                    }
                    _drinks.value = filtered
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
    
    fun setTypeFilter(type: String?) {
        _selectedType.value = type
        loadDrinks()
    }
    
    fun setTasteFilter(taste: String?) {
        _selectedTaste.value = taste
        loadDrinks()
    }
}


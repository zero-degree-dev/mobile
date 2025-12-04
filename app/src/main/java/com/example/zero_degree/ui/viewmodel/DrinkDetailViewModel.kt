package com.example.zero_degree.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.data.model.Drink
import com.example.zero_degree.data.model.Review
import com.example.zero_degree.data.repository.DrinkRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

// ViewModel для детальной страницы напитка
class DrinkDetailViewModel : ViewModel() {
    
    private val drinkRepository = DrinkRepository()
    
    // Напиток
    private val _drink = MutableLiveData<Drink?>()
    val drink: LiveData<Drink?> = _drink
    
    // Отзывы
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews
    
    // Избранное
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite
    
    // Состояние загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    init {
        _isFavorite.value = false
        _reviews.value = emptyList()
    }
    
    // Загрузить напиток по ID
    fun loadDrink(drinkId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val drinkData = drinkRepository.getDrinkById(drinkId)
                _drink.value = drinkData
                
                // Загружаем отзывы
                val reviewsList = drinkRepository.getDrinkReviews(drinkId)
                _reviews.value = reviewsList
            } catch (e: Exception) {
                // В случае ошибки
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Переключить избранное
    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }
    
    // Добавить отзыв
    fun addReview(review: Review) {
        viewModelScope.launch {
            val drinkId = _drink.value?.id ?: return@launch
            val newReview = drinkRepository.addReview(drinkId, review)
            if (newReview != null) {
                // Обновляем список отзывов
                loadDrink(drinkId)
            }
        }
    }
}


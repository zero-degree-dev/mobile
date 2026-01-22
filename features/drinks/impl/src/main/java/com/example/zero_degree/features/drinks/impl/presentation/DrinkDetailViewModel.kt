package com.example.zero_degree.features.drinks.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.ReviewRepository
import com.example.zero_degree.core.api.model.CreateReviewRequest
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.api.model.Review
import com.example.zero_degree.features.drinks.api.DrinkRepository
import com.example.zero_degree.features.drinks.impl.DrinkRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DrinkDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application.applicationContext)
    private val reviewRepository: ReviewRepository = ReviewRepository(application.applicationContext)
    
    private val _drink = MutableStateFlow<Drink?>(null)
    val drink = _drink.asStateFlow()
    
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews = _reviews.asStateFlow()
    
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _reviewCreated = MutableStateFlow(false)
    val reviewCreated = _reviewCreated.asStateFlow()
    
    fun loadDrink(drinkId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            drinkRepository.getDrinkById(drinkId).fold(
                onSuccess = { drinkData ->
                    _drink.value = drinkData
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
    
    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }
    
    fun loadReviews(drinkId: String) {
        viewModelScope.launch {
            reviewRepository.getReviews(drinkId, "drink").fold(
                onSuccess = { reviewsList ->
                    _reviews.value = reviewsList
                },
                onFailure = { }
            )
        }
    }
    
    fun createReview(drinkId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            val request = CreateReviewRequest(drinkId, "drink", rating, comment)
            reviewRepository.createReview(request).fold(
                onSuccess = {
                    _reviewCreated.value = true
                    loadReviews(drinkId) // Перезагружаем отзывы
                },
                onFailure = { }
            )
        }
    }
    
    fun resetReviewCreated() {
        _reviewCreated.value = false
    }
}


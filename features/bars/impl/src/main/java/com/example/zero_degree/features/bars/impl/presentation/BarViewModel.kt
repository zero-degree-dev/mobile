package com.example.zero_degree.features.bars.impl.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero_degree.core.api.ReviewRepository
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.api.model.CreateReviewRequest
import com.example.zero_degree.core.api.model.Review
import com.example.zero_degree.features.bars.api.BarRepository
import com.example.zero_degree.features.bars.impl.BarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BarViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: BarRepository = BarRepositoryImpl(application.applicationContext)
    private val reviewRepository: ReviewRepository = ReviewRepository(application.applicationContext)
    
    private val _bar = MutableStateFlow<Bar?>(null)
    val bar = _bar.asStateFlow()
    
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews = _reviews.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _reviewCreated = MutableStateFlow(false)
    val reviewCreated = _reviewCreated.asStateFlow()
    
    fun loadBar(barId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getBarById(barId).fold(
                onSuccess = { barData ->
                    _bar.value = barData
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
    
    fun loadFirstBar() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getBars().fold(
                onSuccess = { bars ->
                    _bar.value = bars.firstOrNull()
                },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }
    
    fun loadReviews(barId: String) {
        viewModelScope.launch {
            reviewRepository.getReviews(barId, "bar").fold(
                onSuccess = { reviewsList ->
                    _reviews.value = reviewsList
                },
                onFailure = { }
            )
        }
    }
    
    fun createReview(barId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            val request = CreateReviewRequest(barId, "bar", rating, comment)
            reviewRepository.createReview(request).fold(
                onSuccess = {
                    _reviewCreated.value = true
                    loadReviews(barId) // Перезагружаем отзывы
                },
                onFailure = { }
            )
        }
    }
    
    fun resetReviewCreated() {
        _reviewCreated.value = false
    }
}


package com.example.zero_degree.features.drinks.impl.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DrinkCatalogViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkCatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrinkCatalogViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


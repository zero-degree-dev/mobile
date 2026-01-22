package com.example.zero_degree.features.bars.impl.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BarViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarViewModel(application) as T
        }
        if (modelClass.isAssignableFrom(BarsMapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarsMapViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


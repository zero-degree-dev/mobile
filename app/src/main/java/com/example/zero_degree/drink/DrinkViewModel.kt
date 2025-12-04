package com.example.zero_degree.drink

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Drink(val name: String, val description: String, val imageUrl: String, )  {

}

class DrinkViewModel : ViewModel() {

    private val _drink = MutableStateFlow<Drink?>(null)
    val drink: Flow<Drink?> = _drink.asStateFlow()


    fun loadDrink() {
        _drink.value = Drink("Heineken 0.0", "Лёгкое безалкогольное пиво с мягким солодовым вкусом и освежающим ароматом.", "")
    }
}

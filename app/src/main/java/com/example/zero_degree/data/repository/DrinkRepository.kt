package com.example.zero_degree.data.repository

import com.example.zero_degree.data.api.RetrofitClient
import com.example.zero_degree.data.model.Drink
import com.example.zero_degree.data.model.Review

// Репозиторий для работы с напитками
class DrinkRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // Моковые данные напитков
    private val mockDrinks = listOf(
        Drink(
            id = 1,
            name = "Heineken 0.0",
            description = "Лёгкое безалкогольное пиво с мягким солодовым вкусом и освежающим ароматом. Идеально для тех, кто ценит качество и вкус.",
            imageUrl = "https://drinkdrystore.com/cdn/shop/files/Heineken-04_73b71041-cee6-4305-9622-d0e5f0beae05.webp?v=1762512523&width=1500",
            type = "пиво",
            taste = "горький",
            price = 250.0
        ),
        Drink(
            id = 2,
            name = "Coca-Cola Zero",
            description = "Освежающий безалкогольный газированный напиток без сахара. Классический вкус колы с нулевой калорийностью.",
            imageUrl = "https://www.taosushilab.ru/upload/resize_cache/webp/iblock/3e9/errp1r1kuw7zy7vzxoh1yoqr31w62re3/Cola_sait_1.webp",
            type = "лимонад",
            taste = "сладкий",
            price = 180.0
        ),
        Drink(
            id = 3,
            name = "Балтика 0",
            description = "Безалкогольное пиво с насыщенным вкусом и ароматом хмеля. Отлично утоляет жажду.",
            imageUrl = "https://baltika4you.ru/images/detailed/11/%D0%91%D0%B0%D0%BB%D1%82%D0%B8%D0%BA%D0%B0_%D0%AF%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE_bol_1600x1600-04__1_.png",
            type = "пиво",
            taste = "горький",
            price = 220.0
        ),
        Drink(
            id = 4,
            name = "Fanta Апельсин",
            description = "Яркий и сочный безалкогольный газированный напиток с натуральным апельсиновым вкусом.",
            imageUrl = "https://fcdelivery.ru/upload/iblock/400/40025c4f6b39a59aaf0680651701c90a.jpg",
            type = "лимонад",
            taste = "сладкий",
            price = 190.0
        ),
        Drink(
            id = 5,
            name = "Schweppes Тоник",
            description = "Классический безалкогольный тоник с характерной горчинкой. Идеален для коктейлей или как самостоятельный напиток.",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQykTRblREXlmeXzVq0WZ2sIrOWLHsmF6ocw&s",
            type = "лимонад",
            taste = "горький",
            price = 200.0
        ),
        Drink(
            id = 6,
            name = "Sprite",
            description = "Освежающий лимонно-лаймовый безалкогольный напиток. Чистый и бодрящий вкус.",
            imageUrl = "https://rollandia.pl/uploads/Napoje/Sprite.webp",
            type = "лимонад",
            taste = "сладкий",
            price = 180.0
        )
    )
    
    // Моковые отзывы
    private val mockReviews = mapOf(
        1 to listOf(
            Review(
                id = 1,
                drinkId = 1,
                userId = 1,
                userName = "Алексей",
                rating = 5,
                comment = "Отличное безалкогольное пиво! Вкус очень похож на оригинальное Heineken.",
                date = "2024-01-15"
            ),
            Review(
                id = 2,
                drinkId = 1,
                userId = 2,
                userName = "Мария",
                rating = 4,
                comment = "Хороший вариант для тех, кто за рулём. Вкус приятный, не слишком горькое.",
                date = "2024-01-20"
            ),
            Review(
                id = 3,
                drinkId = 1,
                userId = 3,
                userName = "Дмитрий",
                rating = 5,
                comment = "Лучшее безалкогольное пиво из всех, что пробовал. Рекомендую!",
                date = "2024-02-01"
            )
        ),
        2 to listOf(
            Review(
                id = 4,
                drinkId = 2,
                userId = 4,
                userName = "Елена",
                rating = 4,
                comment = "Классический вкус колы без сахара. Отлично подходит для диеты.",
                date = "2024-01-18"
            ),
            Review(
                id = 5,
                drinkId = 2,
                userId = 5,
                userName = "Игорь",
                rating = 5,
                comment = "Любимый напиток! Покупаю постоянно.",
                date = "2024-01-25"
            )
        )
    )
    
    // Получить все напитки с фильтрами
    suspend fun getDrinks(type: String? = null, taste: String? = null): List<Drink> {
        return try {
            val response = apiService.getDrinks(type, taste)
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body() ?: getMockDrinks(type, taste)
            } else {
                getMockDrinks(type, taste)
            }
        } catch (e: Exception) {
            // В случае ошибки возвращаем моковые данные
            getMockDrinks(type, taste)
        }
    }
    
    // Получить напиток по ID
    suspend fun getDrinkById(id: Int): Drink? {
        return try {
            val response = apiService.getDrinkById(id)
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                getMockDrinkById(id)
            }
        } catch (e: Exception) {
            getMockDrinkById(id)
        }
    }
    
    // Получить отзывы для напитка
    suspend fun getDrinkReviews(drinkId: Int): List<Review> {
        return try {
            val response = apiService.getDrinkReviews(drinkId)
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body() ?: getMockReviews(drinkId)
            } else {
                getMockReviews(drinkId)
            }
        } catch (e: Exception) {
            getMockReviews(drinkId)
        }
    }
    
    // Получить моковые напитки с фильтрацией
    private fun getMockDrinks(type: String? = null, taste: String? = null): List<Drink> {
        var drinks = mockDrinks
        if (type != null) {
            drinks = drinks.filter { it.type == type }
        }
        if (taste != null) {
            drinks = drinks.filter { it.taste == taste }
        }
        return drinks
    }
    
    // Получить моковый напиток по ID
    private fun getMockDrinkById(id: Int): Drink? {
        return mockDrinks.find { it.id == id }
    }
    
    // Получить моковые отзывы
    private fun getMockReviews(drinkId: Int): List<Review> {
        return mockReviews[drinkId] ?: emptyList()
    }
    
    // Добавить отзыв
    suspend fun addReview(drinkId: Int, review: Review): Review? {
        return try {
            val response = apiService.addReview(drinkId, review)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}




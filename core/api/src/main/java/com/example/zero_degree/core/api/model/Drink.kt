package com.example.zero_degree.core.api.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

// Адаптер для преобразования строки в Double для цены
class PriceAdapter : JsonDeserializer<Double> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Double {
        return when {
            json == null -> 0.0
            json.isJsonPrimitive && json.asJsonPrimitive.isString -> json.asString.toDoubleOrNull() ?: 0.0
            json.isJsonPrimitive && json.asJsonPrimitive.isNumber -> json.asDouble
            else -> 0.0
        }
    }
}

// Модель напитка
data class Drink(
    val id: String, // UUID
    val name: String,
    val description: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    val type: String? = null, // тип напитка (пиво, лимонад и т.д.)
    val taste: String? = null, // вкус (сладкий, горький и т.д.)
    @JsonAdapter(PriceAdapter::class)
    val price: Double = 0.0,
    val available: Boolean = true, // доступность
    val alcoholContent: Double = 0.0 // всегда 0 для безалкогольных
)


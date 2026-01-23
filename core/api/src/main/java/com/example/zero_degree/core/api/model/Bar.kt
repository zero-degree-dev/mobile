package com.example.zero_degree.core.api.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

// Адаптер для преобразования строки в Double
class StringToDoubleAdapter : JsonDeserializer<Double> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Double {
        return when {
            json == null -> 0.0
            json.isJsonPrimitive && json.asJsonPrimitive.isString -> json.asString.toDoubleOrNull() ?: 0.0
            json.isJsonPrimitive && json.asJsonPrimitive.isNumber -> json.asDouble
            else -> 0.0
        }
    }
}

// Модель бара
data class Bar(
    val id: String, // UUID
    val name: String,
    val address: String,
    @JsonAdapter(StringToDoubleAdapter::class)
    val latitude: Double = 0.0,
    @JsonAdapter(StringToDoubleAdapter::class)
    val longitude: Double = 0.0,
    @SerializedName("openHours")
    val openHours: OpenHours? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    val description: String? = null,
    val phone: String? = null,
    @SerializedName("bookings")
    val bookings: List<Booking>? = null,
    @SerializedName("events")
    val events: List<Event>? = null,
    @SerializedName("drinks")
    val drinks: List<Drink>? = null
)


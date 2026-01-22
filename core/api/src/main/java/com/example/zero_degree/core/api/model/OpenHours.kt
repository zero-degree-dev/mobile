package com.example.zero_degree.core.api.model

import com.google.gson.annotations.SerializedName

// Модель времени работы
data class TimeRange(
    val from: String, // формат "HH:mm"
    val to: String    // формат "HH:mm"
)

// Модель часов работы бара
data class OpenHours(
    @SerializedName("monday")
    val monday: List<TimeRange>? = null,
    @SerializedName("tuesday")
    val tuesday: List<TimeRange>? = null,
    @SerializedName("wednesday")
    val wednesday: List<TimeRange>? = null,
    @SerializedName("thursday")
    val thursday: List<TimeRange>? = null,
    @SerializedName("friday")
    val friday: List<TimeRange>? = null,
    @SerializedName("saturday")
    val saturday: List<TimeRange>? = null,
    @SerializedName("sunday")
    val sunday: List<TimeRange>? = null
)


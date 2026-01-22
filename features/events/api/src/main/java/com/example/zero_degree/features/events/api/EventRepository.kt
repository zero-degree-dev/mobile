package com.example.zero_degree.features.events.api

import com.example.zero_degree.core.api.model.Event

interface EventRepository {
    suspend fun getEvents(barId: String? = null, date: String? = null): Result<List<Event>>
    suspend fun getEventById(id: String): Result<Event>
}


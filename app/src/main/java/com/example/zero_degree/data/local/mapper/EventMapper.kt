package com.example.zero_degree.data.local.mapper

import com.example.zero_degree.data.local.entity.EventEntity
import com.example.zero_degree.data.model.Event

object EventMapper {
    
    fun toEntity(event: Event): EventEntity {
        return EventEntity(
            id = event.id,
            name = event.name,
            description = event.description,
            date = event.date,
            barId = event.barId,
            imageUrl = event.imageUrl
        )
    }
    
    fun toModel(entity: EventEntity): Event {
        return Event(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            date = entity.date,
            barId = entity.barId,
            imageUrl = entity.imageUrl
        )
    }
    
    fun toModelList(entities: List<EventEntity>): List<Event> {
        return entities.map { toModel(it) }
    }
    
    fun toEntityList(events: List<Event>): List<EventEntity> {
        return events.map { toEntity(it) }
    }
}


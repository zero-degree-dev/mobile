package com.example.zero_degree.core.storage.mapper

import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.storage.entity.BarEntity

object BarMapper {
    
    fun toEntity(bar: Bar): BarEntity {
        return BarEntity(
            id = bar.id,
            name = bar.name,
            address = bar.address,
            latitude = bar.latitude,
            longitude = bar.longitude,
            capacity = 0, // capacity больше не используется в новой модели
            imageUrl = bar.imageUrl,
            description = bar.description,
            phone = bar.phone
        )
    }
    
    fun toModel(entity: BarEntity): Bar {
        return Bar(
            id = entity.id,
            name = entity.name,
            address = entity.address,
            latitude = entity.latitude,
            longitude = entity.longitude,
            openHours = null, // openHours не хранится в БД
            imageUrl = entity.imageUrl,
            description = entity.description,
            phone = entity.phone,
            bookings = null,
            events = null,
            drinks = null
        )
    }
    
    fun toModelList(entities: List<BarEntity>): List<Bar> {
        return entities.map { toModel(it) }
    }
    
    fun toEntityList(bars: List<Bar>): List<BarEntity> {
        return bars.map { toEntity(it) }
    }
}


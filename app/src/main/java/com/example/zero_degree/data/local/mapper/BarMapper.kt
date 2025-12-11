package com.example.zero_degree.data.local.mapper

import com.example.zero_degree.data.local.entity.BarEntity
import com.example.zero_degree.data.model.Bar

object BarMapper {
    
    fun toEntity(bar: Bar): BarEntity {
        return BarEntity(
            id = bar.id,
            name = bar.name,
            address = bar.address,
            latitude = bar.latitude,
            longitude = bar.longitude,
            capacity = bar.capacity,
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
            capacity = entity.capacity,
            imageUrl = entity.imageUrl,
            description = entity.description,
            phone = entity.phone
        )
    }
    
    fun toModelList(entities: List<BarEntity>): List<Bar> {
        return entities.map { toModel(it) }
    }
    
    fun toEntityList(bars: List<Bar>): List<BarEntity> {
        return bars.map { toEntity(it) }
    }
}


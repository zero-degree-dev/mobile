package com.example.zero_degree.core.storage.mapper

import android.util.Log
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.storage.entity.BarEntity

object BarMapper {
    
    private const val TAG = "BarMapper"
    
    fun toEntity(bar: Bar): BarEntity {
        val entity = BarEntity(
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
        Log.d(TAG, "toEntity: Конвертация Bar(id=${bar.id}, name=${bar.name}) -> BarEntity(id=${entity.id}, name=${entity.name})")
        return entity
    }
    
    fun toModel(entity: BarEntity): Bar {
        val model = Bar(
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
        Log.d(TAG, "toModel: Конвертация BarEntity(id=${entity.id}, name=${entity.name}) -> Bar(id=${model.id}, name=${model.name})")
        return model
    }
    
    fun toModelList(entities: List<BarEntity>): List<Bar> {
        return entities.map { toModel(it) }
    }
    
    fun toEntityList(bars: List<Bar>): List<BarEntity> {
        return bars.map { toEntity(it) }
    }
}


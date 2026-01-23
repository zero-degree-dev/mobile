package com.example.zero_degree.core.storage.mapper

import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.storage.entity.DrinkEntity

object DrinkMapper {
    
    fun toEntity(drink: Drink): DrinkEntity {
        return DrinkEntity(
            id = drink.id,
            name = drink.name,
            description = drink.description,
            imageUrl = drink.imageUrl,
            type = drink.type,
            taste = drink.taste,
            price = drink.price,
            available = drink.available,
            alcoholContent = drink.alcoholContent
        )
    }
    
    fun toModel(entity: DrinkEntity): Drink {
        return Drink(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imageUrl = entity.imageUrl,
            type = entity.type,
            taste = entity.taste,
            price = entity.price,
            available = entity.available,
            alcoholContent = entity.alcoholContent
        )
    }
    
    fun toModelList(entities: List<DrinkEntity>): List<Drink> {
        return entities.map { toModel(it) }
    }
    
    fun toEntityList(drinks: List<Drink>): List<DrinkEntity> {
        return drinks.map { toEntity(it) }
    }
}


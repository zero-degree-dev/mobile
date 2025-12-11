package com.example.zero_degree.data.local.mapper

import com.example.zero_degree.data.local.entity.DrinkEntity
import com.example.zero_degree.data.model.Drink

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


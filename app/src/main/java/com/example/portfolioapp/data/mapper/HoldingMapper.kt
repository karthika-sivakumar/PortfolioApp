package com.example.portfolioapp.data.mapper

import com.example.portfolioapp.data.local.entity.HoldingEntity
import com.example.portfolioapp.data.remote.dto.HoldingDto
import com.example.portfolioapp.domain.model.Holding

/**
 * DTO → Entity
 */
fun HoldingDto.toEntity(): HoldingEntity {
    return HoldingEntity(
        symbol = symbol,
        name = name,
        quantity = quantity,
        averageCost = averageCost,
        currentPrice = currentPrice,
        currency = currency
    )
}

/**
 * Entity → Domain
 */
fun HoldingEntity.toDomain(): Holding {
    return Holding(
        symbol = symbol,
        name = name,
        quantity = quantity,
        averageCost = averageCost,
        currentPrice = currentPrice,
        currency = currency
    )
}

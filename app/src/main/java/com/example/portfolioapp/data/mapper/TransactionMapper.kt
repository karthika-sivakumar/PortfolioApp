package com.example.portfolioapp.data.mapper

import com.example.portfolioapp.data.local.entity.TransactionEntity
import com.example.portfolioapp.data.remote.dto.TransactionDto
import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.domain.model.TransactionType
private fun mapToTransactionType(type: String): TransactionType {
    return TransactionType.entries.firstOrNull {
        it.name == type.uppercase()
    } ?: TransactionType.BUY   // Safe fallback
}

/**
 * DTO → Entity
 */
fun TransactionDto.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        symbol = symbol,
        type = type.uppercase(),   // Normalize to avoid case issues
        date = date,
        quantity = quantity,
        price = price
    )
}

/**
 * Entity → Domain
 */
fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        symbol = symbol,
        type = mapToTransactionType(type),
        date = date,
        quantity = quantity,
        price = price
    )
}

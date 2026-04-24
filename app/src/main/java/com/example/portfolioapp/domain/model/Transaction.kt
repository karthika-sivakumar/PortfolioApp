package com.example.portfolioapp.domain.model

/**
 * Transaction type enum
 */
enum class TransactionType {
    BUY,
    SELL
}

/**
 * Core business model representing a transaction
 */
data class Transaction(
    val id: Long,
    val symbol: String,
    val type: TransactionType,
    val date: String,
    val quantity: Double,
    val price: Double
) {

    val totalAmount: Double
        get() = quantity * price
}

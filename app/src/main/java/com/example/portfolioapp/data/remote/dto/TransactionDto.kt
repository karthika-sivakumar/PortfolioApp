package com.example.portfolioapp.data.remote.dto

data class TransactionDto(
    val id: Long,
    val symbol: String,
    val type: String,
    val date: String,
    val quantity: Double,
    val price: Double
)

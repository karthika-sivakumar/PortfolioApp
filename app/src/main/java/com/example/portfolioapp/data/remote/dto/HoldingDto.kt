package com.example.portfolioapp.data.remote.dto

data class HoldingDto(
    val symbol: String,
    val name: String,
    val quantity: Double,
    val averageCost: Double,
    val currentPrice: Double,
    val currency: String
)


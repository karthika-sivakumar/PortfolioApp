package com.example.portfolioapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for holdings table
 */
@Entity(tableName = "holdings")
data class HoldingEntity(

    @PrimaryKey
    val symbol: String,        // Unique identifier
    val name: String,
    val quantity: Double,
    val averageCost: Double,
    val currentPrice: Double,
    val currency: String
)

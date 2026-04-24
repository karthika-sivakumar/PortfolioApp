package com.example.portfolioapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for transactions table
 */
@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val symbol: String,        // Foreign key reference (logical)
    val type: String,          // BUY / SELL
    val date: String,
    val quantity: Double,
    val price: Double
)

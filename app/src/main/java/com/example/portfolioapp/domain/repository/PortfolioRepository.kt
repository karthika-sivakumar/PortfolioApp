package com.example.portfolioapp.domain.repository

import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

/**
 * Data layer abstraction for portfolio operations.
 */
interface PortfolioRepository {

    /**
     * Streams the list of all current holdings.
     */
    fun getHoldings(): Flow<List<Holding>>

    /**
     * Synchronizes local data with the remote source.
     */
    suspend fun refreshHoldings()

    /**
     * Streams transactions for a specific asset, with optional type filtering.
     */
    fun getTransactions(
        symbol: String,
        filter: TransactionType? = null
    ): Flow<List<Transaction>>
}
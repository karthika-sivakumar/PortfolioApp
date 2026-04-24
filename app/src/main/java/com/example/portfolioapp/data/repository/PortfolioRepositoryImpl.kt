package com.example.portfolioapp.data.repository

import androidx.room.withTransaction
import com.example.portfolioapp.data.local.dao.HoldingDao
import com.example.portfolioapp.data.local.dao.TransactionDao
import com.example.portfolioapp.data.local.database.AppDatabase
import com.example.portfolioapp.data.mapper.toDomain
import com.example.portfolioapp.data.mapper.toEntity
import com.example.portfolioapp.data.remote.dto.JsonDataSource
import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.domain.model.TransactionType
import com.example.portfolioapp.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository implementation using Room as Single Source of Truth
 * and JSON as refresh source.
 */
class PortfolioRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val holdingDao: HoldingDao,
    private val transactionDao: TransactionDao,
    private val jsonDataSource: JsonDataSource
) : PortfolioRepository {

    /**
     * Observe holdings from Room.
     * UI reacts automatically to DB updates.
     */
    override fun getHoldings(): Flow<List<Holding>> {
        return holdingDao.getAllHoldings()
            .map { entities -> entities.map { it.toDomain() } }
    }

    /**
     * Refresh data from JSON → Room.
     * Throws exception so ViewModel can emit error UI state.
     */
    override suspend fun refreshHoldings() {
        // 1. Fetch remote data (can throw)
        val holdingDtos = jsonDataSource.getHoldings()
        val transactionDtos = jsonDataSource.getTransactions()

        // 2. Map to entities
        val holdingEntities = holdingDtos.map { it.toEntity() }
        val transactionEntities = transactionDtos.map { it.toEntity() }

        try {
            // 3. Atomic DB update
            database.withTransaction {
                holdingDao.clearHoldings()
                transactionDao.clearTransactions()

                holdingDao.insertHoldings(holdingEntities)
                transactionDao.insertTransactions(transactionEntities)
            }
        } catch (e: Exception) {
            // Preserve original cause (better than wrapping blindly)
            throw RuntimeException("Failed to refresh portfolio data", e)
        }
    }

    /**
     * Get transactions for a holding with optional BUY/SELL filter.
     */
    override fun getTransactions(
        symbol: String,
        filter: TransactionType?
    ): Flow<List<Transaction>> {

        val source = if (filter == null) {
            transactionDao.getTransactions(symbol)
        } else {
            transactionDao.getTransactionsByType(
                symbol,
                filter.name
            )
        }

        return source.map { entities ->
            entities.map { it.toDomain() }
        }
    }
}

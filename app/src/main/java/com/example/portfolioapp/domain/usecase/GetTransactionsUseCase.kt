package com.example.portfolioapp.domain.usecase

import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.domain.model.TransactionType
import com.example.portfolioapp.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to fetch transactions for a holding
 */
class GetTransactionsUseCase(
    private val repository: PortfolioRepository
) {

    // Returns filtered transaction stream
    operator fun invoke(
        symbol: String,
        filter: TransactionType? = null
    ): Flow<List<Transaction>> {
        return repository.getTransactions(symbol, filter)
    }
}

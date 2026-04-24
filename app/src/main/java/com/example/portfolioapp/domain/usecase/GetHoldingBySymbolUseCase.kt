package com.example.portfolioapp.domain.usecase

import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case to fetch a single holding by its symbol
 */
class GetHoldingBySymbolUseCase(
    private val repository: PortfolioRepository
) {

    operator fun invoke(symbol: String): Flow<Holding?> {
        return repository.getHoldings()
            .map { holdings ->
                holdings.find { it.symbol == symbol }
            }
    }
}

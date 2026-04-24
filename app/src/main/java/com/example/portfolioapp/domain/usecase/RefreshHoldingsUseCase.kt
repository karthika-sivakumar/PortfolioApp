package com.example.portfolioapp.domain.usecase

import com.example.portfolioapp.domain.repository.PortfolioRepository

/**
 * Use case to refresh holdings from data source
 */
class RefreshHoldingsUseCase(
    private val repository: PortfolioRepository
) {

    // Triggers data sync (JSON → Room later)
    suspend operator fun invoke() {
        repository.refreshHoldings()
    }
}

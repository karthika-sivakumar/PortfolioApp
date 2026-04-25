package com.example.portfolioapp.presentation.detail

import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.presentation.state.UiState

data class DetailState(
    val holdingUiState: UiState<Holding> = UiState.Loading,
    val transactionsUiState: UiState<List<Transaction>> = UiState.Loading,
    val selectedFilter: TransactionFilter = TransactionFilter.ALL
)

enum class TransactionFilter {
    ALL, BUY, SELL
}
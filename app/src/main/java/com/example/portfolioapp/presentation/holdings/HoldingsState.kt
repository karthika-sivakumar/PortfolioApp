package com.example.portfolioapp.presentation.holdings

import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.presentation.state.UiState

/**
 * Represents complete UI state for Holdings screen.
 */
data class HoldingsState(
    val uiState: UiState<List<Holding>> = UiState.Loading,
    val isRefreshing: Boolean = false
)


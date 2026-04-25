package com.example.portfolioapp.presentation.holdings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolioapp.domain.repository.PortfolioRepository
import com.example.portfolioapp.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val repository: PortfolioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HoldingsState())

    // Expose as immutable StateFlow — no stateIn() needed on MutableStateFlow
    val state: StateFlow<HoldingsState> = _state.asStateFlow()

    init {
        // Start refresh first, then observe Room
        refreshData()
        observeHoldings()
    }

    /**
     * Observes Room DB — single source of truth.
     * Updates uiState on every emission.
     */
    private fun observeHoldings() {
        repository.getHoldings()
            .onEach { holdings ->
                _state.update { current ->
                    current.copy(
                        uiState = if (holdings.isEmpty()) UiState.Empty
                        else UiState.Success(holdings)
                    )
                }
            }
            .catch { e ->
                _state.update { current ->
                    current.copy(
                        uiState = UiState.Error(e.message ?: "Failed to load portfolio")
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Pull-to-refresh: fetches from JSON → writes to Room.
     * Room observer picks up the change automatically.
     */
    fun refreshData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            try {
                repository.refreshHoldings()
            } catch (e: Exception) {
                _state.update { current ->
                    current.copy(
                        uiState = UiState.Error(e.message ?: "Failed to refresh portfolio")
                    )
                }
            } finally {
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }
}
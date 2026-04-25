package com.example.portfolioapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolioapp.domain.model.TransactionType
import com.example.portfolioapp.domain.repository.PortfolioRepository
import com.example.portfolioapp.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingDetailViewModel @Inject constructor(
    private val repository: PortfolioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val symbol: String =
        checkNotNull(savedStateHandle["symbol"])

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            DetailState()
        )

    init {
        observeHolding()
        observeTransactions()
    }

    private fun observeHolding() {
        viewModelScope.launch {
            repository.getHoldings()
                .map { list -> list.find { it.symbol == symbol } }
                .collect { holding ->
                    _state.update {
                        if (holding == null) {
                            it.copy(holdingUiState = UiState.Empty)
                        } else {
                            it.copy(holdingUiState = UiState.Success(holding))
                        }
                    }
                }
        }
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            _state.map { it.selectedFilter }
                .distinctUntilChanged()
                .flatMapLatest { filter ->
                    repository.getTransactions(
                        symbol = symbol,
                        filter = when (filter) {
                            TransactionFilter.ALL -> null
                            TransactionFilter.BUY -> TransactionType.BUY
                            TransactionFilter.SELL -> TransactionType.SELL
                        }
                    )
                }
                .collect { transactions ->
                    _state.update {
                        if (transactions.isEmpty()) {
                            it.copy(transactionsUiState = UiState.Empty)
                        } else {
                            it.copy(
                                transactionsUiState = UiState.Success(
                                    transactions.take(20)
                                )
                            )
                        }
                    }
                }
        }
    }

    fun setFilter(filter: TransactionFilter) {
        _state.update { it.copy(selectedFilter = filter) }
    }

    /**
     * Re-triggers data observation (used for retry from UI)
     */
    fun reload() {
        observeHolding()
        observeTransactions()
    }
}
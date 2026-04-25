package com.example.portfolioapp.presentation.holdings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.presentation.components.EmptyView
import com.example.portfolioapp.presentation.components.ErrorView
import com.example.portfolioapp.presentation.components.LoadingView
import com.example.portfolioapp.presentation.state.UiState
import com.example.portfolioapp.presentation.theme.loss
import com.example.portfolioapp.presentation.theme.profit
import com.example.portfolioapp.presentation.components.HoldingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoldingsScreen(
    viewModel: HoldingsViewModel = hiltViewModel(),
    onHoldingClick: (String) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = { HoldingsTopBar(state = state) }
    ) { padding ->

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.refreshData() },
            state = pullRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (val uiState = state.uiState) {

                UiState.Loading -> LoadingView()

                UiState.Empty -> EmptyView(
                    title = "No Holdings Found",
                    message = "Your portfolio is empty. Pull down to refresh."
                )

                is UiState.Error -> ErrorView(
                    message = uiState.message,
                    onRetry = { viewModel.refreshData() }
                )

                is UiState.Success -> HoldingsContent(
                    holdings = uiState.data,
                    onHoldingClick = onHoldingClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HoldingsTopBar(state: HoldingsState) {
    val totalValue = when (val uiState = state.uiState) {
        is UiState.Success -> uiState.data.sumOf { it.currentValue }
        else -> 0.0
    }

    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "My Portfolio",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "₹${"%.2f".format(totalValue)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}


@Composable
private fun HoldingsContent(
    holdings: List<Holding>,
    onHoldingClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            PortfolioSummaryCard(holdings)
        }

        items(
            items = holdings,
            key = { it.symbol }
        ) { holding ->
            HoldingCard(
                holding = holding,
                onClick = onHoldingClick
            )
        }
    }
}


@Composable
private fun PortfolioSummaryCard(
    holdings: List<Holding>
) {
    val totalInvested = holdings.sumOf { it.investedValue }
    val totalCurrent = holdings.sumOf { it.currentValue }
    val totalPnL = totalCurrent - totalInvested

    val pnlPercent = if (totalInvested > 0.0) {
        (totalPnL / totalInvested) * 100
    } else 0.0

    val pnlColor = if (totalPnL >= 0) {
        MaterialTheme.colorScheme.profit
    } else {
        MaterialTheme.colorScheme.loss
    }

    val sign = if (totalPnL >= 0) "+" else ""

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Portfolio Summary",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        text = "Invested",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatCurrency(totalInvested),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Current Value",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatCurrency(totalCurrent),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Overall P&L",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column(horizontalAlignment = Alignment.End) {

                    Text(
                        text = "$sign${formatCurrency(totalPnL)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = pnlColor
                    )

                    Text(
                        text = "$sign${formatPercent(pnlPercent)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = pnlColor
                    )
                }
            }
        }
    }
}



private fun formatCurrency(value: Double): String {
    return "₹${"%.2f".format(value)}"
}

private fun formatPercent(value: Double): String {
    return "${"%.2f".format(value)}%"
}
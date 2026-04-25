package com.example.portfolioapp.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.portfolioapp.presentation.components.*
import com.example.portfolioapp.presentation.state.UiState
import com.example.portfolioapp.presentation.theme.loss
import com.example.portfolioapp.presentation.theme.profit
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoldingDetailScreen(
    onBack: () -> Unit,
    viewModel: HoldingDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Overview", "Transactions")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (val ui = state.holdingUiState) {
                            is UiState.Success -> ui.data.symbol
                            else -> "Details"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> OverviewTab(
                    state = state,
                    onRetry = { viewModel.reload() }
                )
                1 -> TransactionsTab(
                    state = state,
                    onRetry = { viewModel.reload() },
                    onFilterChange = viewModel::setFilter
                )
            }
        }
    }
}

@Composable
private fun OverviewTab(
    state: DetailState,
    onRetry: () -> Unit
) {
    when (val ui = state.holdingUiState) {

        UiState.Loading -> LoadingView()

        UiState.Empty -> EmptyView(
            title = "No Data",
            message = "Holding not found"
        )

        is UiState.Error -> ErrorView(
            message = ui.message,
            onRetry = onRetry
        )

        is UiState.Success -> {
            val holding = ui.data

            val pnlColor = if (holding.profitLoss >= 0)
                MaterialTheme.colorScheme.profit
            else
                MaterialTheme.colorScheme.loss

            val sign = if (holding.profitLoss >= 0) "+" else ""

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text(
                                text = "₹${"%.2f".format(holding.currentPrice)}",
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .background(
                                        pnlColor.copy(alpha = 0.15f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$sign₹${"%.2f".format(holding.profitLoss)}  ($sign${"%.2f".format(holding.profitLossPercentage)}%)",
                                    color = pnlColor,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatItem(
                                "Quantity",
                                holding.quantity.toBigDecimal()
                                    .stripTrailingZeros()
                                    .toPlainString()
                            )
                            StatItem(
                                "Avg Cost",
                                "₹${"%.2f".format(holding.averageCost)}",
                                Alignment.End
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatItem(
                                "Total Cost",
                                "₹${"%.2f".format(holding.investedValue)}"
                            )
                            StatItem(
                                "Market Value",
                                "₹${"%.2f".format(holding.currentValue)}",
                                Alignment.End
                            )
                        }
                    }
                }

                item {
                    Column {
                        Text(
                            text = "Position Performance",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(6.dp))

                        val normalized = (abs(holding.profitLossPercentage) / 50.0)
                            .coerceIn(0.0, 1.0)
                            .toFloat()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(50)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(normalized)
                                    .background(pnlColor, RoundedCornerShape(50))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionsTab(
    state: DetailState,
    onRetry: () -> Unit,
    onFilterChange: (TransactionFilter) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionFilter.entries.forEach { filter ->

                val selected = state.selectedFilter == filter

                FilterChip(
                    selected = selected,
                    onClick = { onFilterChange(filter) },
                    label = { Text(filter.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        when (val ui = state.transactionsUiState) {

            UiState.Loading -> LoadingView()

            UiState.Empty -> EmptyView(
                title = "No Transactions",
                message = "Try a different filter"
            )

            is UiState.Error -> ErrorView(
                message = ui.message,
                onRetry = onRetry
            )

            is UiState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ui.data) { tx ->
                        TransactionItem(tx)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(horizontalAlignment = horizontalAlignment) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
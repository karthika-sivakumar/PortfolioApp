package com.example.portfolioapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.presentation.theme.profit
import com.example.portfolioapp.presentation.theme.loss

@Composable
fun HoldingCard(
    holding: Holding,
    onClick: (String) -> Unit
) {
    val pnlColor = if (holding.profitLoss >= 0) {
        MaterialTheme.colorScheme.profit
    } else {
        MaterialTheme.colorScheme.loss
    }

    val sign = if (holding.profitLoss >= 0) "+" else ""

    Card(
        onClick = { onClick(holding.symbol) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {

            // 🔹 Accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(pnlColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {

                // 🔹 Top Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text(
                            text = holding.symbol,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = holding.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = formatCurrency(holding.currentPrice),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Qty + Avg
                Text(
                    text = "Qty: ${holding.quantity}  •  Avg: ${formatCurrency(holding.averageCost)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 P&L
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "P&L",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(horizontalAlignment = Alignment.End) {

                        Text(
                            text = "$sign${formatCurrency(holding.profitLoss)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = pnlColor
                        )

                        Text(
                            text = "$sign${formatPercent(holding.profitLossPercentage)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = pnlColor
                        )
                    }
                }
            }
        }
    }
}

// 🔹 Helpers

private fun formatCurrency(value: Double): String {
    return "₹${"%.2f".format(value)}"
}

private fun formatPercent(value: Double): String {
    return "${"%.2f".format(value)}%"
}
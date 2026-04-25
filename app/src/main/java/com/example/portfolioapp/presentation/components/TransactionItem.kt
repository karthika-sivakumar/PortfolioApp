package com.example.portfolioapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.domain.model.TransactionType
import com.example.portfolioapp.presentation.theme.profit
import com.example.portfolioapp.presentation.theme.loss
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transaction: Transaction
) {
    val isBuy = transaction.type == TransactionType.BUY

    val bgColor = if (isBuy) {
        MaterialTheme.colorScheme.profit.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.loss.copy(alpha = 0.15f)
    }

    val textColor = if (isBuy) {
        MaterialTheme.colorScheme.profit
    } else {
        MaterialTheme.colorScheme.loss
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 Left Side (Type + Date + Qty)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                // BUY / SELL pill
                Box(
                    modifier = Modifier
                        .background(bgColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = transaction.type.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor
                    )
                }

                Text(
                    text = formatDate(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Qty: ${transaction.quantity} • ₹${formatCurrency(transaction.price)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // 🔹 Right Side (Total value)
            Text(
                text = formatCurrency(transaction.quantity * transaction.price),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


private fun formatCurrency(value: Double): String {
    return "₹${"%.2f".format(value)}"
}

private fun formatDate(date: String): String {
    return try {
        val parsed = LocalDate.parse(date)
        parsed.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: Exception) {
        date
    }
}
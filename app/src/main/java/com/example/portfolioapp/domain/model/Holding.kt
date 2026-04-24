package com.example.portfolioapp.domain.model

/**
 * Represents a stock holding in the portfolio
 */
data class Holding(
    val symbol: String,        // Stock ticker (e.g., AAPL)
    val name: String,          // Company name
    val quantity: Double,      // Number of shares owned
    val averageCost: Double,   // Average buying price per share
    val currentPrice: Double,  // Current market price per share
    val currency: String       // Currency (e.g., USD, INR)
) {

    // Total money invested
    val investedValue: Double
        get() = quantity * averageCost

    // Current total value of holding
    val currentValue: Double
        get() = quantity * currentPrice

    // Profit or loss amount
    val profitLoss: Double
        get() = currentValue - investedValue

    // Profit/loss percentage
    val profitLossPercentage: Double
        get() = if (investedValue > 0.0) {
            (profitLoss / investedValue) * 100
        } else 0.0

    // Overall performance status
    val performance: Performance
        get() = when {
            profitLoss > 0 -> Performance.PROFIT
            profitLoss < 0 -> Performance.LOSS
            else -> Performance.BREAKEVEN
        }

    // Convenience flag for UI usage
    val isProfitable: Boolean
        get() = performance == Performance.PROFIT

    init {
        // Validation to prevent invalid data
        require(quantity >= 0) { "Quantity cannot be negative" }
    }
}

/**
 * Represents profit/loss state of holding
 */
enum class Performance {
    PROFIT,
    LOSS,
    BREAKEVEN
}


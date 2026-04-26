package com.example.portfolioapp.domain.model

import org.junit.Assert.*
import org.junit.Test

class HoldingTest {

    private fun makeHolding(
        quantity: Double = 10.0,
        averageCost: Double = 100.0,
        currentPrice: Double = 120.0
    ) = Holding(
        symbol = "TEST",
        name = "Test Corp",
        quantity = quantity,
        averageCost = averageCost,
        currentPrice = currentPrice,
        currency = "INR"
    )

    @Test
    fun investedValue_isQuantityTimesAverageCost() {
        val h = makeHolding(quantity = 10.0, averageCost = 200.0)
        assertEquals(2000.0, h.investedValue, 0.001)
    }

    @Test
    fun currentValue_isQuantityTimesCurrentPrice() {
        val h = makeHolding(quantity = 10.0, currentPrice = 250.0)
        assertEquals(2500.0, h.currentValue, 0.001)
    }

    @Test
    fun profitLoss_isPositiveWhenPriceIncreases() {
        val h = makeHolding(10.0, 100.0, 150.0)
        assertEquals(500.0, h.profitLoss, 0.001)
        assertEquals(Performance.PROFIT, h.performance)
        assertTrue(h.isProfitable)
    }

    @Test
    fun profitLoss_isNegativeWhenPriceDecreases() {
        val h = makeHolding(10.0, 100.0, 80.0)
        assertEquals(-200.0, h.profitLoss, 0.001)
        assertEquals(Performance.LOSS, h.performance)
        assertFalse(h.isProfitable)
    }

    @Test
    fun profitLossPercentage_calculatesCorrectly() {
        val h = makeHolding(10.0, 100.0, 110.0)
        assertEquals(10.0, h.profitLossPercentage, 0.001)
    }

    @Test
    fun profitLossPercentage_returnsZeroWhenInvestedIsZero() {
        val h = makeHolding(quantity = 0.0, averageCost = 0.0)
        assertEquals(0.0, h.profitLossPercentage, 0.001)
    }

    @Test
    fun performance_isBreakeven_whenPriceUnchanged() {
        val h = makeHolding(averageCost = 100.0, currentPrice = 100.0)
        assertEquals(Performance.BREAKEVEN, h.performance)
    }

    @Test(expected = IllegalArgumentException::class)
    fun negativeQuantity_throwsIllegalArgumentException() {
        makeHolding(quantity = -1.0)
    }
}
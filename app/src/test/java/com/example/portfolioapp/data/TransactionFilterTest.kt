package com.example.portfolioapp.data

import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.domain.model.TransactionType
import org.junit.Assert.*
import org.junit.Test

class TransactionFilterTest {

    private fun makeTransaction(
        type: TransactionType
    ) = Transaction(
        id = 1L,
        symbol = "RELIANCE",
        type = type,
        date = "2024-01-01",
        quantity = 10.0,
        price = 2500.0
    )

    private fun filterTransactions(
        transactions: List<Transaction>,
        filter: TransactionType?
    ): List<Transaction> {
        return if (filter == null) {
            transactions
        } else {
            transactions.filter { it.type == filter }
        }
    }

    @Test
    fun filter_returnsOnlyBuyTransactions() {
        val list = listOf(
            makeTransaction(TransactionType.BUY),
            makeTransaction(TransactionType.SELL),
            makeTransaction(TransactionType.BUY)
        )

        val result = filterTransactions(list, TransactionType.BUY)

        assertEquals(2, result.size)
        assertTrue(result.all { it.type == TransactionType.BUY })
    }

    @Test
    fun filter_returnsOnlySellTransactions() {
        val list = listOf(
            makeTransaction(TransactionType.BUY),
            makeTransaction(TransactionType.SELL)
        )

        val result = filterTransactions(list, TransactionType.SELL)

        assertEquals(1, result.size)
        assertEquals(TransactionType.SELL, result[0].type)
    }

    @Test
    fun filter_returnsAll_whenFilterIsNull() {
        val list = listOf(
            makeTransaction(TransactionType.BUY),
            makeTransaction(TransactionType.SELL)
        )

        val result = filterTransactions(list, null)

        assertEquals(2, result.size)
    }

    @Test
    fun filter_returnsEmpty_whenNoMatchingType() {
        val list = listOf(
            makeTransaction(TransactionType.BUY),
            makeTransaction(TransactionType.BUY)
        )

        val result = filterTransactions(list, TransactionType.SELL)

        assertTrue(result.isEmpty())
    }

    @Test
    fun filter_handlesEmptyList() {
        val result = filterTransactions(emptyList(), TransactionType.BUY)
        assertTrue(result.isEmpty())
    }
}
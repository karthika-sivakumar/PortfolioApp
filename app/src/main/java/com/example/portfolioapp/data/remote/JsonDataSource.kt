package com.example.portfolioapp.data.remote.dto

import android.content.Context
import com.example.portfolioapp.data.remote.dto.HoldingDto
import com.example.portfolioapp.data.remote.dto.TransactionDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * Reads JSON from assets and parses into DTOs.
 *
 * NOTE:
 * - No try-catch here → errors must propagate upward
 * - Repository/ViewModel will handle error states
 */
class JsonDataSource @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {

    /**
     * Reads and parses holdings JSON
     */
    fun getHoldings(): List<HoldingDto> {
        val json = readJsonFromAssets("holdings.json")

        val type = object : TypeToken<List<HoldingDto>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Reads and parses transactions JSON
     */
    fun getTransactions(): List<TransactionDto> {
        val json = readJsonFromAssets("transactions.json")

        val type = object : TypeToken<List<TransactionDto>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Helper to read JSON file from assets
     */
    private fun readJsonFromAssets(fileName: String): String {
        return context.assets
            .open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}

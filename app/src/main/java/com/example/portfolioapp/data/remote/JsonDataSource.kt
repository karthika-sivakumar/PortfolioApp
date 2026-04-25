package com.example.portfolioapp.data.remote

import android.content.Context
import com.example.portfolioapp.data.remote.dto.HoldingDto
import com.example.portfolioapp.data.remote.dto.TransactionDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads JSON from assets and parses into DTOs.
 *
 * NOTE:
 * - No try-catch here → errors propagate to Repository/ViewModel
 */
@Singleton
class JsonDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {

    fun getHoldings(): List<HoldingDto> {
        val json = readJsonFromAssets("holdings.json")
        val type = object : TypeToken<List<HoldingDto>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getTransactions(): List<TransactionDto> {
        val json = readJsonFromAssets("transactions.json")
        val type = object : TypeToken<List<TransactionDto>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun readJsonFromAssets(fileName: String): String {
        return context.assets
            .open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}

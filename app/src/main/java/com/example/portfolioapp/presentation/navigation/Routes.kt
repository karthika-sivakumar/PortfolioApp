package com.example.portfolioapp.presentation.navigation

object Routes {
    const val HOLDINGS = "holdings"
    const val DETAIL = "detail/{symbol}"

    fun detail(symbol: String): String = "detail/$symbol"
}
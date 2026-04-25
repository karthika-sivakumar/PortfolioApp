package com.example.portfolioapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.portfolioapp.presentation.holdings.HoldingsScreen
import com.example.portfolioapp.presentation.detail.HoldingDetailScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOLDINGS
    ) {

        // 🔹 Holdings Screen
        composable(Routes.HOLDINGS) {
            HoldingsScreen(
                onHoldingClick = { symbol ->
                    navController.navigate(Routes.detail(symbol))
                }
            )
        }

        // 🔹 Detail Screen
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("symbol") {
                    type = NavType.StringType
                }
            )
        ) {
            HoldingDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
package com.example.portfolioapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.portfolioapp.presentation.navigation.NavGraph
import com.example.portfolioapp.presentation.theme.PortfolioTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity of the app.
 * Acts as the entry point for the UI.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PortfolioTheme {
                NavGraph()
            }
        }
    }
}
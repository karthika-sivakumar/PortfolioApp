package com.example.portfolioapp.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Navy900,
    onBackground = Silver100,

    surface = Navy800,
    onSurface = Silver100,

    surfaceVariant = Navy700,
    onSurfaceVariant = Silver400,

    primary = Teal400,
    onPrimary = Navy900,

    secondary = Teal300,
    onSecondary = Navy900,

    error = Rose500,
    onError = Silver100
)

/**
 * Semantic color for profit (gain)
 */
val ColorScheme.profit: Color
    get() = Emerald500

/**
 * Semantic color for loss
 */
val ColorScheme.loss: Color
    get() = Rose500

@Composable
fun PortfolioTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
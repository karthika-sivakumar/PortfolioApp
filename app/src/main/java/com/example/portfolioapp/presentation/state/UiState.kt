package com.example.portfolioapp.presentation.state

/**
 * Represents UI states explicitly.
 * No silent fallbacks allowed.
 */
sealed class UiState<out T> {

    object Loading : UiState<Nothing>()

    data class Success<T>(
        val data: T
    ) : UiState<T>()

    data class Error(
        val message: String
    ) : UiState<Nothing>()

    object Empty : UiState<Nothing>()
}
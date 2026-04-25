package com.example.portfolioapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Entry point for Hilt dependency injection.
 * Initializes the dependency graph at app startup.
 */
@HiltAndroidApp
class PortfolioApp : Application()

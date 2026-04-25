package com.example.portfolioapp.di

import android.content.Context
import androidx.room.Room
import com.example.portfolioapp.data.local.dao.HoldingDao
import com.example.portfolioapp.data.local.dao.TransactionDao
import com.example.portfolioapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "portfolio_db"
        ).fallbackToDestructiveMigration() // Added for easier development
            .build()
    }

    /**
     * DAOs don't need @Singleton because they are managed by the AppDatabase instance.
     */
    @Provides
    fun provideHoldingDao(database: AppDatabase): HoldingDao {
        return database.holdingDao()
    }

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }
}
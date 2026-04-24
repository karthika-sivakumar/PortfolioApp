package com.example.portfolioapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.portfolioapp.data.local.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE symbol = :symbol ORDER BY date DESC LIMIT 20")
    fun getTransactions(symbol: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE symbol = :symbol AND type = :type 
        ORDER BY date DESC LIMIT 20
    """)
    fun getTransactionsByType(
        symbol: String,
        type: String
    ): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()
}

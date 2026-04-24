package com.example.portfolioapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.portfolioapp.data.local.dao.HoldingDao
import com.example.portfolioapp.data.local.dao.TransactionDao
import com.example.portfolioapp.data.local.entity.HoldingEntity
import com.example.portfolioapp.data.local.entity.TransactionEntity

@Database(
    entities = [HoldingEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun holdingDao(): HoldingDao
    abstract fun transactionDao(): TransactionDao
}

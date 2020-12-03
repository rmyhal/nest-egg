package com.rmyhal.shared.cache

import com.rmyhal.shared.cache.entity.Balance
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllBalances(): Flow<List<Balance>> {
        return dbQuery.selectAllBalances { name, amount, currencyCode ->
            Balance(
                name,
                amount,
                currencyCode
            )
        }
            .asFlow()
            .mapToList()
    }

    fun insertBalance(balance: Balance) {
        dbQuery.insertBalance(balance.name, balance.amount, balance.currencyCode)
    }
}
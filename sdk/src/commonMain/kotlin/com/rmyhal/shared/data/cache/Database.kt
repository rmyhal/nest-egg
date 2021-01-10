package com.rmyhal.shared.data.cache

import com.rmyhal.shared.data.cache.entity.BalanceEntity
import com.rmyhal.shared.data.cache.entity.ExchangeRate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllBalances(): Flow<List<BalanceEntity>> {
        return dbQuery.getAllBalances { name, amount, currencyCode ->
            BalanceEntity(
                name,
                amount,
                currencyCode
            )
        }
            .asFlow()
            .mapToList()
    }

    fun insertBalance(balanceEntity: BalanceEntity) {
        dbQuery.insertBalance(balanceEntity.name, balanceEntity.amount, balanceEntity.currencyCode)
    }

    fun getRatesForCurrency(baseCurrency: String): Map<String, Float> {
        return dbQuery.getRatesForCurrency(baseCurrency) { to, rate, _ -> to to rate }
            .executeAsList()
            .toMap()
    }

    fun insertRate(exchangeRate: ExchangeRate) {
        dbQuery.insertExchangeRate(exchangeRate.currencyTo, exchangeRate.rate, exchangeRate.currencyFrom)
    }
}
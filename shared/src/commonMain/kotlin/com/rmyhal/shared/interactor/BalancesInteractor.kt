package com.rmyhal.shared.interactor

import com.rmyhal.shared.cache.Database
import com.rmyhal.shared.cache.entity.Balance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BalancesInteractor internal constructor(private val database: Database) {

    val totalBalance: Flow<Float> = database.getAllBalances()
        .map(::calculateTotalBalance)

    fun getBalances(): Flow<List<Balance>> = database.getAllBalances()

    fun saveBalance(name: String, amount: Float, currencyCode: String) {
        database.insertBalance(Balance(name, amount, currencyCode))
    }

    private fun calculateTotalBalance(balances: List<Balance>): Float {
        return balances.map { it.amount }.reduceOrNull { acc, fl -> acc + fl } ?: 0F
    }
}
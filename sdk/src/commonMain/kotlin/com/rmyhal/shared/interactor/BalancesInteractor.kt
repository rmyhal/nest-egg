package com.rmyhal.shared.interactor

import com.rmyhal.shared.data.cache.Database
import com.rmyhal.shared.data.cache.entity.BalanceEntity
import com.rmyhal.shared.entity.Balance
import com.rmyhal.shared.entity.TotalBalance
import kotlinx.coroutines.flow.*

class BalancesInteractor internal constructor(
    private val database: Database,
    private val ratesInteractor: RatesInteractor
) {

    fun getBalances(): Flow<List<Balance>> = database.getAllBalances()
        .transform { balances -> emit(balances.map { Balance(it.name, it.amount, it.currencyCode) }) }

    fun saveBalance(name: String, amount: Float, currencyCode: String) {
        database.insertBalance(BalanceEntity(name, amount, currencyCode))
    }

    fun getTotalBalance(baseCurrency: String): Flow<TotalBalance> {
        return database.getAllBalances()
            .map { balances ->
                val rates = ratesInteractor.getRatesForCurrency(baseCurrency)
                var result = 0f
                balances.forEachIndexed { index, balance ->
                    result += balance.amount / rates.getValue(balances[index].currencyCode)
                }
                TotalBalance(result, baseCurrency)
            }
    }

    fun deleteBalance() {
        // TODO not implemented
    }
}
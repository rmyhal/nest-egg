package com.rmyhal.shared.interactor

import com.rmyhal.shared.currentTimeMillis
import com.rmyhal.shared.data.api.RatesApi
import com.rmyhal.shared.data.cache.Database
import com.rmyhal.shared.data.cache.Prefs
import com.rmyhal.shared.data.cache.entity.ExchangeRate
import com.rmyhal.shared.data.api.entity.RatesResponse

class RatesInteractor internal constructor(
    private val api: RatesApi,
    private val database: Database,
    private val prefs: Prefs,
    currenciesInteractor: CurrenciesInteractor,
) {

    companion object {
        private const val RATES_OUTDATED_TIME_MILLIS = 86_400_000L // 1 day
    }

    private val currencies = currenciesInteractor.supportedCurrencies

    suspend fun getRatesForCurrency(baseCurrency: String): Map<String, Float> {
        val cachedRates = database.getRatesForCurrency(baseCurrency)
        return if (cachedRates.isEmpty() || areRatesOutdated()) {
            return api.getExchangeRates(baseCurrency, currencies.joinToString(","))
                .also { response ->
                    saveRates(response)
                    saveRatesRequestTime()
                }
                .rates
        } else {
            cachedRates
        }
    }

    private fun saveRates(response: RatesResponse) {
        currencies.forEach { currencyFrom ->
            val currencyFromRate = response.rates.getValue(currencyFrom)
            currencies.forEach { currencyTo ->
                val currencyToRateOld = response.rates.getValue(currencyTo)
                val currencyToRateNew = currencyToRateOld / currencyFromRate
                database.insertRate(
                    ExchangeRate(currencyTo, currencyToRateNew, currencyFrom)
                )
            }
        }
    }

    private fun saveRatesRequestTime() {
        prefs.ratesSyncTime = currentTimeMillis()
    }

    private fun areRatesOutdated(): Boolean {
        val diff = currentTimeMillis() - prefs.ratesSyncTime
        return prefs.ratesSyncTime == -1L || diff > RATES_OUTDATED_TIME_MILLIS
    }
}
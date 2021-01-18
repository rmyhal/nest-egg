package com.rmyhal.shared

import com.rmyhal.shared.data.api.RatesApi
import com.rmyhal.shared.data.cache.Database
import com.rmyhal.shared.data.cache.DatabaseDriverFactory
import com.rmyhal.shared.data.cache.Prefs
import com.rmyhal.shared.interactor.BalancesInteractor
import com.rmyhal.shared.interactor.CurrenciesInteractor
import com.rmyhal.shared.interactor.RatesInteractor
import com.rmyhal.shared.util.HttpClientFactory
import kotlinx.serialization.json.Json

open class NestEgg internal constructor(
    private val prefs: Prefs,
    httpClientFactory: HttpClientFactory,
    driverFactory: DatabaseDriverFactory
) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    private val database = Database(driverFactory)
    private val ratesApi = RatesApi(httpClientFactory.create(json, true))

    fun balancesInteractor(): BalancesInteractor = BalancesInteractor(database, ratesInteractor())
    fun ratesInteractor(): RatesInteractor = RatesInteractor(ratesApi, database, prefs, currenciesInteractor())
    fun currenciesInteractor(): CurrenciesInteractor = CurrenciesInteractor()

    companion object
}
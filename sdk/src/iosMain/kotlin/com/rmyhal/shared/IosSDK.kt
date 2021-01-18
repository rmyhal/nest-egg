package com.rmyhal.shared

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.rmyhal.shared.adapter.IosBalancesInteractor
import com.rmyhal.shared.adapter.IosCurrenciesInteractor
import com.rmyhal.shared.adapter.IosRatesInteractor
import com.rmyhal.shared.data.cache.DatabaseDriverFactory
import com.rmyhal.shared.data.cache.Prefs
import com.rmyhal.shared.util.HttpClientFactory

class IosSDK(
    databaseDriverFactory: DatabaseDriverFactory,
    isDebug: Boolean
) {

    init {
        if (isDebug) Napier.base(DebugAntilog())
    }

    private val sdk = NestEgg(
        Prefs(),
        HttpClientFactory { createHttpEngine() },
        databaseDriverFactory
    )

    fun balancesInteractor() = IosBalancesInteractor(sdk.balancesInteractor())
    fun ratesInteractor() = IosRatesInteractor(sdk.ratesInteractor())
    fun currenciesInteractor() = IosCurrenciesInteractor(sdk.currenciesInteractor())
}
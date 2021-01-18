package com.rmyhal.shared.adapter

import com.rmyhal.shared.interactor.RatesInteractor
import com.rmyhal.shared.util.MainLoopDispatcher
import com.rmyhal.shared.util.wrap
import kotlinx.coroutines.CoroutineScope

class IosRatesInteractor(private val interactor: RatesInteractor) :
    CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    fun getRatesForCurrency(
        baseCurrency: String,
        callback: (result: Map<String, Float>?, exception: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getRatesForCurrency(baseCurrency) }
    }
}
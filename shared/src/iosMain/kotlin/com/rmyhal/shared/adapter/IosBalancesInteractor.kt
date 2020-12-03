package com.rmyhal.shared.adapter

import com.rmyhal.shared.interactor.BalancesInteractor
import com.rmyhal.shared.util.MainLoopDispatcher
import com.rmyhal.shared.util.wrap
import kotlinx.coroutines.CoroutineScope

class IosBalancesInteractor internal constructor(
    private val interactor: BalancesInteractor
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    val accountBalance = interactor.totalBalance.wrap()

    fun saveBalance(name: String, amount: Float, currencyCode: String) {
        interactor.saveBalance(name, amount, currencyCode)
    }
}
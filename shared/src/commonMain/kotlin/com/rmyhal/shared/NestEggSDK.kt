package com.rmyhal.shared

import com.rmyhal.shared.cache.Database
import com.rmyhal.shared.cache.DatabaseDriverFactory
import com.rmyhal.shared.interactor.BalancesInteractor

open class NestEggSDK internal constructor(
    private val driverFactory: DatabaseDriverFactory,
    private val options: Options.() -> Unit = {}
) {

    private val database = Database(driverFactory)

    fun balanceInteractor(): BalancesInteractor = BalancesInteractor(database)

    class Options private constructor() {

    }

    companion object
}
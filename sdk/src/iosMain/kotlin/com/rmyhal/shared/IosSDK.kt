package com.rmyhal.shared

import com.rmyhal.shared.adapter.IosBalancesInteractor
import com.rmyhal.shared.cache.DatabaseDriverFactory

class IosSDK(databaseDriverFactory: DatabaseDriverFactory) {

    private val sdk = NestEggSDK(databaseDriverFactory)

    fun homeInteractor() = IosBalancesInteractor(sdk.balanceInteractor())
}
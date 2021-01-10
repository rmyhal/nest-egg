package com.rmyhal.shared

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.rmyhal.shared.adapter.IosBalancesInteractor
import com.rmyhal.shared.data.cache.DatabaseDriverFactory
import com.rmyhal.shared.data.cache.Prefs

class IosSDK(
    databaseDriverFactory: DatabaseDriverFactory,
    isDebug: Boolean
) {

    init {
        if (isDebug) Napier.base(DebugAntilog())
    }

    private val sdk = NestEgg(Prefs(), databaseDriverFactory)

    fun homeInteractor() = IosBalancesInteractor(sdk.balancesInteractor())
}
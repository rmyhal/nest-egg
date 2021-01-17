package com.rmyhal.nestegg.di

import com.rmyhal.nestegg.ui.addbalance.AddBalanceFragment
import com.rmyhal.nestegg.ui.addbalance.AddBalanceViewModel
import com.rmyhal.nestegg.ui.balances.BalancesFragment
import com.rmyhal.nestegg.ui.balances.BalancesViewModel
import com.rmyhal.nestegg.ui.global.CurrencyFormatter
import com.rmyhal.nestegg.util.ExceptionHandler
import com.rmyhal.nestegg.system.ResourceManager
import com.rmyhal.nestegg.system.SystemMessageNotifier
import com.rmyhal.shared.NestEgg
import com.rmyhal.shared.create
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentsModule = module {
    fragment { BalancesFragment(get()) }
    fragment { AddBalanceFragment(get()) }
}

private val viewModelsModule = module {
    viewModel { BalancesViewModel(get(), get(), get(), get(), get()) }
    viewModel { AddBalanceViewModel(get(), get(), get(), get()) }
}

private val globalModule = module {
    single { SystemMessageNotifier() }
    single { CurrencyFormatter() }
    single { ResourceManager(androidContext()) }
    single { ExceptionHandler(get(), get()) }
}

private val sdkModule = module {
    single { NestEgg.create(androidContext()) }
    factory { get<NestEgg>().balancesInteractor() }
    factory { get<NestEgg>().currenciesInteractor() }
    factory { get<NestEgg>().ratesInteractor() }
}

val appModules = listOf(sdkModule, viewModelsModule, fragmentsModule, globalModule)
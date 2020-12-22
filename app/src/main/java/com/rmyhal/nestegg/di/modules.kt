package com.rmyhal.nestegg.di

import com.rmyhal.nestegg.ui.addbalance.AddBalanceFragment
import com.rmyhal.nestegg.ui.addbalance.AddBalanceViewModel
import com.rmyhal.nestegg.ui.balances.BalancesFragment
import com.rmyhal.nestegg.ui.balances.BalancesViewModel
import com.rmyhal.nestegg.ui.global.CurrencyFormatter
import com.rmyhal.shared.NestEggSDK
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
    viewModel { BalancesViewModel(get(), get()) }
    viewModel { AddBalanceViewModel(get()) }
}

private val globalModule = module {
    single { CurrencyFormatter() }
}

private val sdkModule = module {
    single { NestEggSDK.create(androidContext()) }
    factory { get<NestEggSDK>().balanceInteractor() }
}

val appModules = listOf(sdkModule, viewModelsModule, fragmentsModule, globalModule)
package com.rmyhal.nestegg.di

import com.rmyhal.nestegg.ui.home.HomeFragment
import com.rmyhal.nestegg.ui.home.HomeViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentsModule = module {
    fragment { HomeFragment(get()) }
}

private val viewModelsModule = module {
    viewModel { HomeViewModel() }
}

val appModules = listOf(fragmentsModule, viewModelsModule)
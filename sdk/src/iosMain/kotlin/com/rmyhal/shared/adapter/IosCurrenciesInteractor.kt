package com.rmyhal.shared.adapter

import com.rmyhal.shared.interactor.CurrenciesInteractor

class IosCurrenciesInteractor(private val interactor: CurrenciesInteractor) {

    val supportedCurrencies = interactor.supportedCurrencies

    fun getLastSelectedCurrencyCode() = interactor.getLastSelectedCurrencyCode()
}
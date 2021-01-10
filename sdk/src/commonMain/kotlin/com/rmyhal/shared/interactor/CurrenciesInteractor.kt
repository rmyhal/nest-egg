package com.rmyhal.shared.interactor

class CurrenciesInteractor internal constructor() {

    companion object {
        private const val DEFAULT_CURRENCY_CODE = "USD"
    }

    val supportedCurrencies = setOf("USD", "EUR", "UAH")

    // TODO (implement caching of last selected currency code)
    fun getLastSelectedCurrencyCode() = DEFAULT_CURRENCY_CODE
}


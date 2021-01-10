package com.rmyhal.nestegg.ui.balances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmyhal.nestegg.ui.global.CurrencyFormatter
import com.rmyhal.shared.interactor.BalancesInteractor
import com.rmyhal.shared.interactor.CurrenciesInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BalancesViewModel(
    private val balancesInteractor: BalancesInteractor,
    private val currenciesInteractor: CurrenciesInteractor,
    private val formatter: CurrencyFormatter
) : ViewModel() {

    private val _props = MutableStateFlow(BalancesFragment.Props())
    val props: StateFlow<BalancesFragment.Props>
        get() = _props

    private var totalBalanceJob: Job? = null
    private var selectedCurrency = currenciesInteractor.getLastSelectedCurrencyCode()
    private val supportedCurrencies: ArrayDeque<String> = ArrayDeque(currenciesInteractor.supportedCurrencies)

    init {
        viewModelScope.launch {
            totalBalanceJob = getTotalBalance(selectedCurrency)
            launch {
                balancesInteractor.getBalances().collect { balances ->
                    _props.value = _props.value.copy(
                        balances = balances.map { balance ->
                            BalancesFragment.Props.Balance(
                                balance.name,
                                formatter.toCurrencyFormat(balance.amount, balance.currencyCode)
                            )
                        }
                    )
                }
            }
        }
    }

    fun onCurrencyClicked() {
        totalBalanceJob?.cancel()
        supportedCurrencies.remove(selectedCurrency)
        if (supportedCurrencies.isEmpty()) {
            supportedCurrencies.addAll(currenciesInteractor.supportedCurrencies)
        }
        selectedCurrency = supportedCurrencies.first()
        totalBalanceJob = getTotalBalance(selectedCurrency)
    }

    private fun getTotalBalance(baseCurrency: String): Job = viewModelScope.launch {
        balancesInteractor.getTotalBalance(baseCurrency)
            .collect { balance ->
                _props.value = _props.value.copy(
                    totalBalance = formatter.formatAmount(balance.amount, 2),
                    currency = formatter.getCurrencySymbol(balance.currency)
                )
            }
    }
}
package com.rmyhal.nestegg.ui.balances

import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmyhal.nestegg.ui.Field
import com.rmyhal.shared.interactor.BalancesInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BalancesViewModel(private val balancesInteractor: BalancesInteractor) : ViewModel() {

    private val formatter = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(DEFAULT_CURRENCY_CODE)
    }

    private val _props = MutableStateFlow(BalancesFragment.Props())
    val props: StateFlow<BalancesFragment.Props>
        get() = _props

    init {
        viewModelScope.launch {
            launch {
                balancesInteractor.totalBalance
                    .collect { balance: Float ->
                        formatter.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE)
                        _props.value = _props.value.copy(
                            totalBalance = Field(formatter.format(balance))
                        )
                    }
            }
            launch {
                balancesInteractor.getBalances()
                    .collect { wallets ->
                        _props.value = _props.value.copy(
                            balances = Field(wallets.map {
                                formatter.currency = Currency.getInstance(it.currencyCode)
                                BalancesFragment.Props.Balance(
                                    it.name,
                                    formatter.format(it.amount)
                                )
                            })
                        )
                    }
            }
        }
    }

    companion object {
        //todo(should be remove and use currency code from [Balance] class)
        private const val DEFAULT_CURRENCY_CODE = "USD"
    }
}
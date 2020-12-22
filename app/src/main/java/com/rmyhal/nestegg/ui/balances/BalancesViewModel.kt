package com.rmyhal.nestegg.ui.balances

import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmyhal.shared.interactor.BalancesInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BalancesViewModel(private val balancesInteractor: BalancesInteractor) : ViewModel() {

    private val format = NumberFormat.getCurrencyInstance().apply {
        isGroupingUsed = true
        maximumFractionDigits = 0
    }

    private val _props = MutableStateFlow(BalancesFragment.Props())
    val props: StateFlow<BalancesFragment.Props>
        get() = _props

    init {
        viewModelScope.launch {
            launch {
                balancesInteractor.totalBalance.collect { balance ->
                    _props.value = _props.value.copy(totalBalance = balance.toString())
                }
            }
            launch {
                balancesInteractor.getBalances().collect { balances ->
                    _props.value = _props.value.copy(
                        balances = balances.map { balance ->
                            format.currency = Currency.getInstance(balance.currencyCode)
                            BalancesFragment.Props.Balance(balance.name, format.format(balance.amount))
                        }
                    )
                }
            }
        }
    }
}
package com.rmyhal.nestegg.ui.balances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmyhal.nestegg.ui.global.CurrencyFormatter
import com.rmyhal.shared.interactor.BalancesInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BalancesViewModel(
    private val balancesInteractor: BalancesInteractor,
    private val formatter: CurrencyFormatter
) : ViewModel() {

    private val _props = MutableStateFlow(BalancesFragment.Props())
    val props: StateFlow<BalancesFragment.Props>
        get() = _props

    init {
        viewModelScope.launch {
            launch {
                balancesInteractor.totalBalance.collect { balance ->
                    _props.value = _props.value.copy(totalBalance = formatter.formatAmount(balance))
                }
            }
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
}
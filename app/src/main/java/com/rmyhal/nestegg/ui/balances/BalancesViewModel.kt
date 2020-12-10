package com.rmyhal.nestegg.ui.balances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmyhal.nestegg.ui.Field
import com.rmyhal.shared.interactor.BalancesInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BalancesViewModel(private val balancesInteractor: BalancesInteractor) : ViewModel() {

    private val _props = MutableStateFlow(BalancesFragment.Props())
    val props: StateFlow<BalancesFragment.Props>
        get() = _props

    init {
        viewModelScope.launch {
            launch {
                balancesInteractor.totalBalance
                    .collect { balance ->
                        _props.value = _props.value.copy(
                            totalBalance = Field(balance.toString())
                        )
                    }
            }
            launch {
                balancesInteractor.getBalances()
                    .collect { balances ->
                        _props.value = _props.value.copy(
                            balances = Field(balances.map { balance ->
                                BalancesFragment.Props.Balance(
                                    balance.name,
                                    balance.amount.toString()
                                )
                            })
                        )
                    }
            }
        }
    }
}
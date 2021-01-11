package com.rmyhal.nestegg.ui.balances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmyhal.nestegg.ui.global.CurrencyFormatter
import com.rmyhal.nestegg.util.ExceptionHandler
import com.rmyhal.shared.interactor.BalancesInteractor
import com.rmyhal.shared.interactor.CurrenciesInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BalancesViewModel(
    private val balancesInteractor: BalancesInteractor,
    private val currenciesInteractor: CurrenciesInteractor,
    private val formatter: CurrencyFormatter,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

    private val _props = MutableStateFlow(BalancesFragment.Props())
    val props: StateFlow<BalancesFragment.Props>
        get() = _props

    private val actionChannel: Channel<Action> = Channel(Channel.RENDEZVOUS)
    val actions: Flow<Action>
        get() = actionChannel.consumeAsFlow()

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

    fun onAction(action: Action) {
        when (action) {
            Action.OnTotalBalanceCurrencyClicked -> changeTotalBalanceCurrency()
            else -> {}
        }
        actionChannel.offer(action)
    }

    private fun changeTotalBalanceCurrency() {
        totalBalanceJob?.cancel()
        supportedCurrencies.remove(selectedCurrency)
        if (supportedCurrencies.isEmpty()) {
            supportedCurrencies.addAll(currenciesInteractor.supportedCurrencies)
        }
        selectedCurrency = supportedCurrencies.first()
        totalBalanceJob = getTotalBalance(selectedCurrency)
    }

    private fun getTotalBalance(baseCurrency: String): Job = viewModelScope.launch(exceptionHandler.handler) {
        balancesInteractor.getTotalBalance(baseCurrency)
            .collect { balance ->
                _props.value = _props.value.copy(
                    totalBalance = formatter.formatAmount(balance.amount, 2),
                    currency = formatter.getCurrencySymbol(balance.currency)
                )
            }
    }

    sealed class Action {
        object OnAddBalanceClicked : Action()
        object OnTotalBalanceCurrencyClicked : Action()
    }
}
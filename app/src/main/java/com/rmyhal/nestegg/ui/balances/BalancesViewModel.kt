package com.rmyhal.nestegg.ui.balances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmyhal.nestegg.system.ResourceManager
import com.rmyhal.nestegg.ui.global.CurrencyFormatter
import com.rmyhal.nestegg.util.ExceptionHandler
import com.rmyhal.shared.entity.Balance
import com.rmyhal.shared.interactor.BalancesInteractor
import com.rmyhal.shared.interactor.CurrenciesInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayDeque

class BalancesViewModel(
    private val balancesInteractor: BalancesInteractor,
    private val currenciesInteractor: CurrenciesInteractor,
    private val formatter: CurrencyFormatter,
    private val exceptionHandler: ExceptionHandler,
    private val resourceManager: ResourceManager
) : ViewModel() {

    private val _props = MutableStateFlow(BalancesFragment.Props())
    val props: StateFlow<BalancesFragment.Props>
        get() = _props

    private val eventChannel: Channel<Event> = Channel(Channel.RENDEZVOUS)
    val events: Flow<Event>
        get() = eventChannel.consumeAsFlow()

    private var totalBalanceJob: Job? = null
    private var selectedCurrency = currenciesInteractor.getLastSelectedCurrencyCode()
    private val supportedCurrencies: ArrayDeque<String> = ArrayDeque(currenciesInteractor.supportedCurrencies)

    private var recentlyDeletedBalance: RecentlyDeletedBalance? = null
    private val localBalances: MutableList<Balance> = mutableListOf()

    init {
        viewModelScope.launch {
            totalBalanceJob = getTotalBalance(selectedCurrency)
            launch {
                balancesInteractor.getBalances().collect { balances ->
                    localBalances.apply { clear(); addAll(balances) }
                    _props.value = _props.value.copy(
                        balances = balances.map { balance ->
                            BalancesFragment.Props.Balance(
                                balance.name,
                                getBalanceAmount(balance),
                                resourceManager.getDrawableResByName(getCurrencyIconName(balance.currencyCode))
                            )
                        }
                    )
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnTotalBalanceCurrencyClicked -> {
                changeTotalBalanceCurrency()
                eventChannel.offer(Event.AnimateCurrencyArrows)
            }
            is Action.OnBalanceSwiped -> {
                val balances = _props.value.balances.toMutableList()
                val balanceToRemove = balances.removeAt(action.position)
                recentlyDeletedBalance = RecentlyDeletedBalance(balanceToRemove, action.position)
                _props.value = _props.value.copy(balances = balances)
                eventChannel.offer(Event.ShowUndoButton)
            }
            Action.OnAddBalanceClicked -> {
                eventChannel.offer(Event.NavigateToAddBalance)
            }
            Action.OnUndoDeleteClicked -> {
                recentlyDeletedBalance?.let { removed ->
                    val newBalances = _props.value.balances.toMutableList().apply { add(removed.position, removed.balance) }
                    _props.value = _props.value.copy(balances = newBalances)
                }
            }
            Action.OnDeleteSnackBarDismissed -> {
                recentlyDeletedBalance?.let { deleteBalance ->
                    val balance = localBalances[deleteBalance.position]
                    balancesInteractor.deleteBalance(balance.name, balance.currencyCode)
                }
                recentlyDeletedBalance = null
            }
        }
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

    private fun getCurrencyIconName(currencyCode: String): String = "ic_${currencyCode.toLowerCase(Locale.getDefault())}_flag"

    private fun getBalanceAmount(balance: Balance) =
        "${balance.currencyCode} – ${formatter.formatAmount(balance.amount, BALANCE_AMOUNT_FRACTION_DIGITS)}"

    sealed class Action {
        object OnAddBalanceClicked : Action()
        object OnTotalBalanceCurrencyClicked : Action()
        class OnBalanceSwiped(val position: Int) : Action()
        object OnUndoDeleteClicked : Action()
        object OnDeleteSnackBarDismissed : Action()
    }

    sealed class Event {
        object NavigateToAddBalance : Event()
        object AnimateCurrencyArrows : Event()
        object ShowUndoButton : Event()
    }

    data class RecentlyDeletedBalance(val balance: BalancesFragment.Props.Balance, val position: Int)

    companion object {
        private const val BALANCE_AMOUNT_FRACTION_DIGITS = 2
    }
}
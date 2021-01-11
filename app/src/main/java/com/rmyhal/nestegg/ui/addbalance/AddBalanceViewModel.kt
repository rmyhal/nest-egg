package com.rmyhal.nestegg.ui.addbalance

import androidx.lifecycle.ViewModel
import com.rmyhal.nestegg.R
import com.rmyhal.nestegg.system.ResourceManager
import com.rmyhal.nestegg.system.SystemMessageNotifier
import com.rmyhal.shared.interactor.BalancesInteractor
import com.rmyhal.shared.interactor.CurrenciesInteractor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow

class AddBalanceViewModel(
    private val balancesInteractor: BalancesInteractor,
    private val systemMessageNotifier: SystemMessageNotifier,
    private val resourceManager: ResourceManager,
    currenciesInteractor: CurrenciesInteractor
) : ViewModel() {

    private val _props =
        MutableStateFlow(AddBalanceFragment.Props(currencies = currenciesInteractor.supportedCurrencies.toList()))
    val props: StateFlow<AddBalanceFragment.Props>
        get() = _props

    private val actionsChannel = Channel<Action>(Channel.RENDEZVOUS)
    val actions: Flow<Action> get() = actionsChannel.consumeAsFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.OnSaveClicked -> onSaveClicked(action.name, action.amount, action.currency)
        }
        actionsChannel.offer(action)
    }

    private fun onSaveClicked(name: String?, amount: String?, currency: String?) {
        val fieldWithError = getErrorFieldOrNull(name, amount, currency)
        if (fieldWithError == null) {
            requireNotNull(name); requireNotNull(amount); requireNotNull(currency)
            balancesInteractor.saveBalance(name, amount.toFloat(), currency)
            _props.value = _props.value.copy(saveStatus = AddBalanceFragment.Props.SaveStatus.Saved)
            systemMessageNotifier.send(resourceManager.getString(R.string.add_balance_saved, name))
        } else {
            _props.value = _props.value.copy(saveStatus = AddBalanceFragment.Props.SaveStatus.Error(fieldWithError))
        }
    }

    private fun getErrorFieldOrNull(
        name: String?,
        amount: String?,
        currencyCode: String?
    ): AddBalanceFragment.Props.SaveStatus.Error.Field? = when {
        name.isNullOrEmpty() -> AddBalanceFragment.Props.SaveStatus.Error.Field.NAME
        amount.isNullOrEmpty() -> AddBalanceFragment.Props.SaveStatus.Error.Field.AMOUNT
        currencyCode.isNullOrEmpty() -> AddBalanceFragment.Props.SaveStatus.Error.Field.CURRENCY
        else -> null
    }

    sealed class Action {
        class OnSaveClicked(val name: String?, val amount: String?, val currency: String?) : Action()
    }
}
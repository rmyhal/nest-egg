package com.rmyhal.nestegg.ui.addbalance

import androidx.lifecycle.ViewModel
import com.rmyhal.shared.interactor.BalancesInteractor
import com.rmyhal.shared.interactor.CurrenciesInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddBalanceViewModel(
    private val balancesInteractor: BalancesInteractor,
    currenciesInteractor: CurrenciesInteractor
) : ViewModel() {

    private val _props =
        MutableStateFlow(AddBalanceFragment.Props(currencies = currenciesInteractor.supportedCurrencies.toList()))
    val props: StateFlow<AddBalanceFragment.Props>
        get() = _props

    fun onSaveClicked(name: String?, amount: String?, currency: String?) {
        val fieldWithError = getErrorFieldOrNull(name, amount, currency)
        if (fieldWithError == null) {
            requireNotNull(name); requireNotNull(amount); requireNotNull(currency)
            balancesInteractor.saveBalance(name, amount.toFloat(), currency)
            _props.value = _props.value.copy(saveStatus = AddBalanceFragment.Props.SaveStatus.Saved)
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
}
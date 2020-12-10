package com.rmyhal.nestegg.ui.addbalance

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddBalanceViewModel : ViewModel() {

    private val _props = MutableStateFlow(AddBalanceFragment.Props())
    val props: StateFlow<AddBalanceFragment.Props>
        get() = _props
}
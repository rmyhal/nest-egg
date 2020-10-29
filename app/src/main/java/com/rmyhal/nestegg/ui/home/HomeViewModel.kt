package com.rmyhal.nestegg.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _props = MutableStateFlow(HomeFragment.HomeProps())
    val props: StateFlow<HomeFragment.HomeProps>
        get() = _props
}
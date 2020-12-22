package com.rmyhal.nestegg.ui.global

import android.icu.text.NumberFormat
import android.icu.util.Currency

class CurrencyFormatter {

    private val formatter = NumberFormat.getCurrencyInstance().apply {
        isGroupingUsed = true
        maximumFractionDigits = 0
    }

    fun format(amount: Float, currencyCode: String): String {
        formatter.currency = Currency.getInstance(currencyCode)
        return formatter.format(amount)
    }
}
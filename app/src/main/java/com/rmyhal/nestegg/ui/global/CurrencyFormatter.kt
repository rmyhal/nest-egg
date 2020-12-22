package com.rmyhal.nestegg.ui.global

import android.icu.text.NumberFormat
import android.icu.util.Currency

class CurrencyFormatter {

    private val currencyFormatter = NumberFormat.getCurrencyInstance().apply {
        isGroupingUsed = true
        maximumFractionDigits = 0
    }

    private val numberFormatter = NumberFormat.getInstance().apply {
        isGroupingUsed = true
    }

    fun toCurrencyFormat(amount: Float, currencyCode: String, fractionDigits: Int = 0): String {
        currencyFormatter.currency = Currency.getInstance(currencyCode)
        currencyFormatter.maximumFractionDigits = fractionDigits
        return currencyFormatter.format(amount)
    }

    fun formatAmount(amount: Float, fractionDigits: Int = 0): String {
        numberFormatter.maximumFractionDigits = fractionDigits
        return numberFormatter.format(amount)
    }
}
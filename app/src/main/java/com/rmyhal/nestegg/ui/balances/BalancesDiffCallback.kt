package com.rmyhal.nestegg.ui.balances

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

class BalancesDiffCallback : DiffUtil.ItemCallback<BalancesFragment.Props.Balance>() {

    override fun areItemsTheSame(
        oldItem: BalancesFragment.Props.Balance,
        newItem: BalancesFragment.Props.Balance
    ) = oldItem.amount == newItem.amount

    override fun areContentsTheSame(
        oldItem: BalancesFragment.Props.Balance,
        newItem: BalancesFragment.Props.Balance
    ) = oldItem == newItem

    override fun getChangePayload(
        oldItem: BalancesFragment.Props.Balance,
        newItem: BalancesFragment.Props.Balance
    ): Any {
        return Bundle().apply {
            putString(PAYLOAD_NAME, newItem.name)
            putString(PAYLOAD_AMOUNT, newItem.amount)
        }
    }

    companion object {
        const val PAYLOAD_NAME = "payload_name"
        const val PAYLOAD_AMOUNT = "payload_name"
    }
}
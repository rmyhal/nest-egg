package com.rmyhal.nestegg.ui.balances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rmyhal.nestegg.databinding.ItemBalanceBinding

class BalancesAdapter :
    ListAdapter<BalancesFragment.Props.Balance, BalancesAdapter.BalanceViewHolder>(BalancesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(
            ItemBalanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            bindFromPayload(payloads[0] as Bundle, holder)
        }
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun bindFromPayload(bundle: Bundle, holder: BalanceViewHolder) {
        if (bundle.containsKey(BalancesDiffCallback.PAYLOAD_NAME)) {
            holder.setTitle(bundle.getString(BalancesDiffCallback.PAYLOAD_NAME)!!)
        }
        if (bundle.containsKey(BalancesDiffCallback.PAYLOAD_AMOUNT)) {
            holder.setAmount(bundle.getString(BalancesDiffCallback.PAYLOAD_AMOUNT)!!)
        }
    }

    inner class BalanceViewHolder(private val binding: ItemBalanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(balance: BalancesFragment.Props.Balance) = with(binding) {
            icon.setImageResource(balance.icon)
            setTitle(balance.name)
            setAmount(balance.amount)
        }

        fun setTitle(newTitle: String) {
            binding.txtTitle.text = newTitle
        }

        fun setAmount(newAmount: String) {
            binding.txtAmount.text = newAmount
        }
    }
}
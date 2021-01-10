package com.rmyhal.nestegg.ui.balances

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rmyhal.nestegg.databinding.ItemBalanceBinding

class BalancesAdapter : RecyclerView.Adapter<BalancesAdapter.BalanceViewHolder>() {

    private val balances: MutableList<BalancesFragment.Props.Balance> = mutableListOf()

    override fun getItemCount() = balances.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(
            ItemBalanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bind(balances[position])
    }

    fun setBalances(newBalances: List<BalancesFragment.Props.Balance>) {
        balances.clear()
        balances.addAll(newBalances)
        notifyDataSetChanged()
    }

    inner class BalanceViewHolder(private val binding: ItemBalanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(balance: BalancesFragment.Props.Balance) = with(binding) {
            txtAmount.text = balance.amount
            txtTitle.text = balance.name
        }
    }
}
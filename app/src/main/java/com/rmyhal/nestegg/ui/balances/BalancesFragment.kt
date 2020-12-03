package com.rmyhal.nestegg.ui.balances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmyhal.nestegg.databinding.FragmentBalancesBinding
import com.rmyhal.nestegg.ui.Field
import com.rmyhal.nestegg.ui.base.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BalancesFragment(private val viewModel: BalancesViewModel) : BaseFragment<FragmentBalancesBinding>() {

    private val balancesAdapter = BalancesAdapter()

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentBalancesBinding {
        return FragmentBalancesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.props.collect(::render)
        }
        initRecycler()
    }

    private fun render(props: Props) {
        binding.txtTotalAmount.text = props.totalBalance.value
        balancesAdapter.setWallets(props.balances.value)
    }

    private fun initRecycler() {
        binding.rvBalances.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvBalances.adapter = balancesAdapter
    }

    data class Props(
        val totalBalance: Field<String> = Field("0"),
        val balances: Field<List<Balance>> = Field(emptyList()),
    ) {
        data class Balance(val name: String, val amount: String)
    }
}
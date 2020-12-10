package com.rmyhal.nestegg.ui.balances

import android.content.Context
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
import java.lang.IllegalStateException

class BalancesFragment(private val viewModel: BalancesViewModel) : BaseFragment<FragmentBalancesBinding>() {

    private val balancesAdapter = BalancesAdapter()
    private lateinit var addBalanceNavigation: AddBalanceNavigation

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentBalancesBinding {
        return FragmentBalancesBinding.inflate(inflater, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addBalanceNavigation = when {
            context is AddBalanceNavigation -> context
            parentFragment is AddBalanceNavigation -> parentFragment as AddBalanceNavigation
            else -> throw IllegalStateException("$context or $parentFragment must implement ${AddBalanceNavigation::class.java}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.props.collect(::render)
        }
        initRecycler()
    }

    private fun render(props: Props) = with(binding) {
        txtTotalAmount.text = props.totalBalance.value
        balancesAdapter.setWallets(props.balances.value)
        fabAddBalance.setOnClickListener {
            addBalanceNavigation.navigateToAddBalance(fabAddBalance)
        }
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

    interface AddBalanceNavigation {
        fun navigateToAddBalance(sharedElement: View)
    }
}
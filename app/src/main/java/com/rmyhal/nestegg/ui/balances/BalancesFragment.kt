package com.rmyhal.nestegg.ui.balances

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmyhal.nestegg.databinding.FragmentBalancesBinding
import com.rmyhal.nestegg.ui.base.BaseFragment
import kotlinx.coroutines.flow.collect

class BalancesFragment(private val viewModel: BalancesViewModel) : BaseFragment<FragmentBalancesBinding>() {

    private val balancesAdapter = BalancesAdapter()
    private var arrowsRotated = false
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
        setupListeners()
    }

    private fun render(props: Props) = with(binding) {
        txtTotalAmount.text = props.totalBalance
        txtTotalCurrency.text = props.currency
        balancesAdapter.setBalances(props.balances)
    }

    private fun initRecycler() {
        binding.rvBalances.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvBalances.adapter = balancesAdapter
    }

    private fun setupListeners() {
        binding.txtTotalCurrency.setOnClickListener {
            animateArrows()
            viewModel.onCurrencyClicked()
        }
        binding.fabAddBalance.setOnClickListener {
            addBalanceNavigation.navigateToAddBalance(binding.fabAddBalance)
        }
    }

    private fun animateArrows() {
        val scale = if (arrowsRotated) 1f else -1f
        binding.imgArrows.animate()
            .scaleY(scale)
            .start()
        arrowsRotated = arrowsRotated.not()
    }

    data class Props(
        val totalBalance: String = "0",
        val currency: String = "",
        val balances: List<Balance> = emptyList(),
    ) {
        data class Balance(val name: String, val amount: String)
    }

    interface AddBalanceNavigation {
        fun navigateToAddBalance(sharedElement: View)
    }
}
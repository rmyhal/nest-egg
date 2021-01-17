package com.rmyhal.nestegg.ui.balances

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.rmyhal.nestegg.R
import com.rmyhal.nestegg.databinding.FragmentBalancesBinding
import com.rmyhal.nestegg.ui.base.BaseFragment
import com.rmyhal.nestegg.ui.global.SwipeToDeleteCallback
import kotlinx.coroutines.flow.collect

class BalancesFragment(private val viewModel: BalancesViewModel) : BaseFragment<FragmentBalancesBinding>() {

    private val swipeToDelete: SwipeToDeleteCallback by lazy(LazyThreadSafetyMode.NONE) {
        SwipeToDeleteCallback(requireContext()) { position ->
            viewModel.onAction(BalancesViewModel.Action.OnBalanceSwiped(position))
        }
    }
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
        lifecycleScope.launchWhenStarted { viewModel.props.collect(::render) }
        lifecycleScope.launchWhenStarted { viewModel.events.collect(::handleEvent) }
        initRecycler()
        setupListeners()
    }

    private fun render(props: Props) = with(binding) {
        txtTotalAmount.text = props.totalBalance
        txtTotalCurrency.text = props.currency
        balancesAdapter.setBalances(props.balances)
    }

    private fun handleEvent(event: BalancesViewModel.Event) {
        when (event) {
            BalancesViewModel.Event.NavigateToAddBalance -> addBalanceNavigation.navigateToAddBalance(binding.fabAddBalance)
            BalancesViewModel.Event.AnimateCurrencyArrows -> animateArrows()
            is BalancesViewModel.Event.DeleteBalance -> {
                balancesAdapter.delete(event.position)
                showUndoSnackBar()
            }
            is BalancesViewModel.Event.RestoreBalance -> balancesAdapter.insert(event.balance, event.position)
        }
    }

    private fun initRecycler() {
        binding.rvBalances.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvBalances.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.rvBalances)
        binding.rvBalances.adapter = balancesAdapter
    }

    private fun setupListeners() {
        binding.txtTotalCurrency.setOnClickListener {
            viewModel.onAction(BalancesViewModel.Action.OnTotalBalanceCurrencyClicked)
        }
        binding.fabAddBalance.setOnClickListener {
            viewModel.onAction(BalancesViewModel.Action.OnAddBalanceClicked)
        }
    }

    private fun animateArrows() {
        val scale = if (arrowsRotated) 1f else -1f
        binding.imgArrows.animate()
            .scaleY(scale)
            .start()
        arrowsRotated = arrowsRotated.not()
    }

    private fun showUndoSnackBar() {
        snackBar(getString(R.string.balances_item_deleted)) {
            setAction(R.string.balances_undo_delete) {
                viewModel.onAction(BalancesViewModel.Action.OnUndoDeleteClicked)
            }
            addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != DISMISS_EVENT_ACTION) {
                        viewModel.onAction(BalancesViewModel.Action.OnDeleteSnackBarDismissed)
                    }
                }
            })
        }
    }

    data class Props(
        val totalBalance: String = "-",
        val currency: String = "/",
        val balances: List<Balance> = emptyList(),
    ) {
        data class Balance(val name: String, val amount: String, val icon: Int)
    }

    interface AddBalanceNavigation {
        fun navigateToAddBalance(sharedElement: View)
    }
}
package com.rmyhal.nestegg.ui.addbalance

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.rmyhal.nestegg.R
import com.rmyhal.nestegg.databinding.FragmentAddBalanceBinding
import com.rmyhal.nestegg.ui.base.BaseFragment
import com.rmyhal.nestegg.ui.global.OnFragmentDismissListener
import com.rmyhal.nestegg.util.themeColor
import kotlinx.coroutines.flow.collect


class AddBalanceFragment(private val viewModel: AddBalanceViewModel) : BaseFragment<FragmentAddBalanceBinding>() {

    private val currenciesAdapter: ArrayAdapter<String> by lazy(LazyThreadSafetyMode.NONE) {
        ArrayAdapter(requireContext(), R.layout.item_dropdown, mutableListOf())
    }

    private val txtCurrency: AutoCompleteTextView
        get() = binding.currencyInputLayout.editText!! as AutoCompleteTextView

    private lateinit var dismissListener: OnFragmentDismissListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dismissListener = when {
            context is OnFragmentDismissListener -> context
            parentFragment is OnFragmentDismissListener -> parentFragment as OnFragmentDismissListener
            else -> throw IllegalStateException("$context or $parentFragment must implement ${OnFragmentDismissListener::class.java}")
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddBalanceBinding {
        return FragmentAddBalanceBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted { viewModel.props.collect(::render) }
        lifecycleScope.launchWhenStarted { viewModel.actions.collect(::handleAction) }
        initCurrencyView()
        setupListeners()
    }

    private fun render(props: Props) = with(binding) {
        currenciesAdapter.clear()
        currenciesAdapter.addAll(props.currencies)
        when (props.saveStatus) {
            Props.SaveStatus.Saved -> dismissListener.dismiss()
            is Props.SaveStatus.Error -> showError(props.saveStatus.field)
            else -> { // ignored
            }
        }
    }

    private fun handleAction(action: AddBalanceViewModel.Action) {

    }

    private fun setupListeners() {
        binding.btnClose.setOnClickListener { dismissListener.dismiss() }
        binding.btnDone.setOnClickListener {
            viewModel.onAction(
                AddBalanceViewModel.Action.OnSaveClicked(
                    binding.txtName.editText?.text.toString(),
                    binding.txtAmount.editText?.text.toString(),
                    txtCurrency.text.toString()
                )
            )
        }
        binding.txtName.editText?.addTextChangedListener {
            if (binding.txtName.error != null) {
                binding.txtName.error = null
            }
        }
        binding.txtAmount.editText?.addTextChangedListener {
            if (binding.txtAmount.error != null) {
                binding.txtAmount.error = null
            }
        }
        binding.currencyInputLayout.editText?.addTextChangedListener {
            if (binding.currencyInputLayout.error != null) {
                binding.currencyInputLayout.error = null
            }
        }
    }

    private fun initCurrencyView() = with(txtCurrency) {
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !isPopupShowing) {
                showDropDown()
            }
        }
        setOnEditorActionListener { view, _, _ ->
            view.clearFocus()
            binding.btnDone.performClick()
            return@setOnEditorActionListener true
        }
        setAdapter(currenciesAdapter)
    }

    private fun showError(fieldWithError: Props.SaveStatus.Error.Field) {
        when (fieldWithError) {
            Props.SaveStatus.Error.Field.NAME -> {
                binding.txtName.error = getString(R.string.add_balance_name_error)
                binding.txtName.requestFocus()
            }
            Props.SaveStatus.Error.Field.AMOUNT -> {
                binding.txtAmount.error = getString(R.string.add_balance_amount_error)
                binding.txtAmount.requestFocus()
            }
            Props.SaveStatus.Error.Field.CURRENCY -> {
                binding.currencyInputLayout.error = getString(R.string.add_balance_currency_error)
                binding.currencyInputLayout.requestFocus()
            }
        }
    }

    private fun initAnimation() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.animation_duration).toLong()
            scrimColor = Color.TRANSPARENT
            setPathMotion(MaterialArcMotion())
            containerColor = requireContext().themeColor(R.attr.colorSurface)
            startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
            endContainerColor = requireContext().themeColor(R.attr.colorSurface)
        }
    }

    data class Props(
        val currencies: List<String> = emptyList(),
        val saveStatus: SaveStatus = SaveStatus.Default
    ) {

        sealed class SaveStatus {
            object Default : SaveStatus()
            object Saved : SaveStatus()
            data class Error(val field: Field) : SaveStatus() {
                enum class Field { NAME, AMOUNT, CURRENCY }
            }
        }
    }
}
package com.rmyhal.nestegg.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rmyhal.nestegg.databinding.FragmentHomeBinding
import com.rmyhal.nestegg.ui.base.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment(private val viewModel: HomeViewModel) : BaseFragment<FragmentHomeBinding>() {

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.props.collect { props ->
                render(props)
            }
        }
    }

    private fun render(props: HomeProps) {
        binding.txtView.text = props.balance
    }

    data class HomeProps(val balance: String = "")
}
package com.rmyhal.nestegg.ui.addbalance

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.transition.Slide
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.rmyhal.nestegg.R
import com.rmyhal.nestegg.databinding.FragmentAddBalanceBinding
import com.rmyhal.nestegg.ui.Field
import com.rmyhal.nestegg.ui.base.BaseFragment
import com.rmyhal.nestegg.util.OnFragmentDismissListener
import com.rmyhal.nestegg.util.themeColor
import kotlinx.coroutines.flow.collect
import java.lang.IllegalStateException

class AddBalanceFragment(private val viewModel: AddBalanceViewModel) : BaseFragment<FragmentAddBalanceBinding>() {

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
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            endViewId = R.id.balanceRoot
            duration = 300L
            scrimColor = Color.TRANSPARENT
            setPathMotion(MaterialArcMotion())
            containerColor = requireContext().themeColor(R.attr.colorSurface)
            startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
            endContainerColor = requireContext().themeColor(R.attr.colorSurface)
        }
        sharedElementReturnTransition = Slide().apply {
            duration = 225L
            addTarget(R.id.balanceRoot)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.props.collect(::render)
        }
        binding.imgClose.setOnClickListener { dismissListener.dismiss() }
    }

    private fun render(props: Props) = with(binding) {
//        txtTitle.text = props.title.value
    }

    data class Props(
        val title: Field<String> = Field("")
    )
}
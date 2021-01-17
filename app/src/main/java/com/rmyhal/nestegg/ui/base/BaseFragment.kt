package com.rmyhal.nestegg.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.rmyhal.nestegg.R

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    /**
     * This property is only valid between onCreateView and onDestroyView
     */
    protected val binding get() = _binding!!
    private var _binding: B? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bind(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun bind(inflater: LayoutInflater, container: ViewGroup?): B

    protected fun snackBar(text: String, duration: Int = Snackbar.LENGTH_LONG, builder: Snackbar.() -> Unit = {}) {
        Snackbar.make(binding.root, text, duration)
            .apply {
                val anchor = findAnchorOrNull()
                if (anchor != null) anchorView = anchor
                apply(builder)
                show()
            }
    }

    /**
     * Find an anchor view for [Snackbar] Possible anchors are [BottomNavigationView] or [FloatingActionButton]
     */
    private fun findAnchorOrNull(): View? = view?.findViewWithTag<FloatingActionButton>(getString(R.string.fab_tag))
}
package com.rmyhal.nestegg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialElevationScale
import com.rmyhal.nestegg.databinding.FragmentAppBinding
import com.rmyhal.nestegg.ui.addbalance.AddBalanceFragment
import com.rmyhal.nestegg.ui.balances.BalancesFragment
import com.rmyhal.nestegg.ui.base.BaseFragment
import com.rmyhal.nestegg.util.OnFragmentDismissListener

class AppFragment : BaseFragment<FragmentAppBinding>(), OnFragmentDismissListener, BalancesFragment.AddBalanceNavigation {

    companion object {
        private const val DEFAULT_SELECTED_BOTTOM_ITEM_ID = R.id.balances
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAppBinding {
        return FragmentAppBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigation()
    }

    override fun navigateToAddBalance(sharedElement: View) {
        currentVisibleFragment()?.apply {
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300L
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 300L
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.container, AddBalanceFragment::class.java, null)
            .addSharedElement(sharedElement, sharedElement.transitionName)
            .addToBackStack("addBalance")
            .commit()
    }

    override fun dismiss() {
        childFragmentManager.popBackStack()
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.balances -> selectTab(BalancesFragment::class.java)
                R.id.goals -> {
                }
            }
            true
        }
        binding.bottomNavigation.selectedItemId = DEFAULT_SELECTED_BOTTOM_ITEM_ID
    }

    private fun selectTab(fragmentClass: Class<out Fragment>, args: Bundle? = null) {
        val fragmentManager = childFragmentManager
        val currentFragment = currentVisibleFragment()
        val tag = fragmentClass.name

        val newFragment = fragmentManager.findFragmentByTag(tag)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) {
            // tab not switching
            return
        }

        val transaction = fragmentManager.beginTransaction()
        if (newFragment == null) {
            transaction.add(R.id.container, fragmentClass, args, tag)
        }

        currentFragment?.let {
            transaction.hide(it)
        }

        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commit()
    }

    private fun currentVisibleFragment(): Fragment? {
        return childFragmentManager.fragments.firstOrNull { frag -> frag.isVisible }
    }
}
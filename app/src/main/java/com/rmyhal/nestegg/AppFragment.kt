package com.rmyhal.nestegg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rmyhal.nestegg.databinding.FragmentAppBinding
import com.rmyhal.nestegg.ui.balances.BalancesFragment
import com.rmyhal.nestegg.ui.base.BaseFragment

class AppFragment : BaseFragment<FragmentAppBinding>() {

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAppBinding {
        return FragmentAppBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.balances -> selectTab(BalancesFragment::class.java)
                R.id.goals -> {}
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

    companion object {
        private const val DEFAULT_SELECTED_BOTTOM_ITEM_ID = R.id.balances
    }
}
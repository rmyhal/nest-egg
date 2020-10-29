package com.rmyhal.nestegg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rmyhal.nestegg.ui.home.HomeFragment
import org.koin.androidx.fragment.android.setupKoinFragmentFactory

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            showHomeFragment()
        }
    }

    private fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment::class.java, null)
            .commit()
    }
}
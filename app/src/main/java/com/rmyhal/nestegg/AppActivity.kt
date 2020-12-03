package com.rmyhal.nestegg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import org.koin.androidx.fragment.android.setupKoinFragmentFactory

class AppActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            showAppFragment()
        }
    }

    private fun showAppFragment() {
        supportFragmentManager.commit {
            replace(R.id.container, AppFragment())
        }
    }
}
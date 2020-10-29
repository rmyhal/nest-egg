package com.rmyhal.nestegg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rmyhal.nestegg.ui.home.HomeFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction()
				.replace(R.id.container, HomeFragment())
				.commit()
		}
	}
}
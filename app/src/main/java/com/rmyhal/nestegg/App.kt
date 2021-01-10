package com.rmyhal.nestegg

import android.app.Application
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.rmyhal.nestegg.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            fragmentFactory()
            modules(appModules)
        }
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }
}
package com.rmyhal.shared.data.cache

import platform.Foundation.NSUserDefaults

internal actual class Prefs {

    companion object {
        private const val RATES_SYNC_TIME = "rates_sync_time"
    }

    actual var ratesSyncTime: Long
        get() = NSUserDefaults.standardUserDefaults.integerForKey(RATES_SYNC_TIME)
        set(value) {
            NSUserDefaults.standardUserDefaults.setInteger(value, RATES_SYNC_TIME)
        }
}
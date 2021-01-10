package com.rmyhal.shared.data.cache

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

internal actual class Prefs(context: Context) {

    companion object {
        private val RATES_SYNC_TIME = preferencesKey<Long>("rates_sync_time")
    }

    private val preferences = context.createDataStore("prefs")

    actual var ratesSyncTime: Long
        get() = runBlocking { preferences.data.firstOrNull()?.get(RATES_SYNC_TIME) ?: -1 }
        set(value) {
            runBlocking {
                preferences.edit { preferences ->
                    preferences[RATES_SYNC_TIME] = value
                }
            }
        }
}
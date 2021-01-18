package com.rmyhal.shared

import android.content.Context
import com.rmyhal.shared.data.cache.DatabaseDriverFactory
import com.rmyhal.shared.data.cache.Prefs
import com.rmyhal.shared.util.HttpClientFactory

fun NestEgg.Companion.create(context: Context) = NestEgg(
    Prefs(context),
    HttpClientFactory { createHttpEngine(context) },
    DatabaseDriverFactory(context)
)
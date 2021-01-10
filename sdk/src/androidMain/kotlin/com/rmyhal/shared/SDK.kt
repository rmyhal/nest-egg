package com.rmyhal.shared

import android.content.Context
import com.rmyhal.shared.data.cache.DatabaseDriverFactory
import com.rmyhal.shared.data.cache.Prefs

fun NestEgg.Companion.create(context: Context) = NestEgg(Prefs(context), DatabaseDriverFactory(context))
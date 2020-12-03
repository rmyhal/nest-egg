package com.rmyhal.shared

import android.content.Context
import com.rmyhal.shared.cache.DatabaseDriverFactory

fun NestEggSDK.Companion.create(
    context: Context,
    options: NestEggSDK.Options.() -> Unit = {}
) = NestEggSDK(
    DatabaseDriverFactory(context),
    options
)
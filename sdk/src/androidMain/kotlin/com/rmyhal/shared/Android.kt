package com.rmyhal.shared

import android.content.Context
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun currentTimeMillis() = System.currentTimeMillis()

internal fun createHttpEngine(
    context: Context,
): HttpClientEngine = OkHttp.create {
    config {
        followRedirects(true)
        followSslRedirects(true)
        retryOnConnectionFailure(true)
    }
}
package com.rmyhal.shared

import io.ktor.client.engine.*
import io.ktor.client.engine.ios.*
import kotlin.system.getTimeMillis

actual fun currentTimeMillis() = getTimeMillis()

internal fun createHttpEngine(): HttpClientEngine = Ios.create {
    configureRequest {
        setAllowsCellularAccess(true)
    }
}
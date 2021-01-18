package com.rmyhal.shared.util

import com.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json

internal class HttpClientFactory(
    private val engineFactory: () -> HttpClientEngine
) {

    fun create(json: Json, enableLogging: Boolean): HttpClient = HttpClient(engineFactory()) {
        if (enableLogging) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "HttpClient", message = message)
                    }
                }
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }
}

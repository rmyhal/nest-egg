package com.rmyhal.shared.data.api

import com.rmyhal.shared.data.api.entity.RatesResponse
import io.ktor.client.*
import io.ktor.client.request.*

internal class RatesApi(private val httpClient: HttpClient) {

    private val endpoint = "https://api.exchangerate.host"

    @Throws(Exception::class)
    suspend fun getExchangeRates(baseCurrency: String, currencies: String): RatesResponse =
        httpClient.get("$endpoint/latest") {
            parameter("base", baseCurrency)
            parameter("symbols", currencies)
        }
}
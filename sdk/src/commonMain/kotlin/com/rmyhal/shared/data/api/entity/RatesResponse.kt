package com.rmyhal.shared.data.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RatesResponse(
    @SerialName("base")
    val base: String,
    @SerialName("rates")
    val rates: Map<String, Float>
)
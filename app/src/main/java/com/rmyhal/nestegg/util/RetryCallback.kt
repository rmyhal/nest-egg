package com.rmyhal.nestegg.util

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class RetryCallback(val callback: () -> Unit) : AbstractCoroutineContextElement(RetryCallback) {
    companion object Key : CoroutineContext.Key<RetryCallback>
}
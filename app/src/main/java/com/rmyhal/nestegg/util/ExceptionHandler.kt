package com.rmyhal.nestegg.util

import com.rmyhal.nestegg.R
import com.rmyhal.nestegg.system.ResourceManager
import com.rmyhal.nestegg.system.SystemMessageNotifier
import kotlinx.coroutines.CoroutineExceptionHandler
import java.io.IOException

class ExceptionHandler(
    private val resourceManager: ResourceManager,
    private val systemMessageNotifier: SystemMessageNotifier
) {

    val handler = CoroutineExceptionHandler { _, throwable ->
        handle(throwable)
    }

    private fun handle(exception: Throwable) {
        exception.printStackTrace()
        val message = when (exception) {
            is IOException -> resourceManager.getString(R.string.connection_error)
            else -> resourceManager.getString(R.string.general_error)
        }
        systemMessageNotifier.send(message)
    }
}
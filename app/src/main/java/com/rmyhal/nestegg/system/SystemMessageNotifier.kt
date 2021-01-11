package com.rmyhal.nestegg.system

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class SystemMessageNotifier {

    private val messageChannel: Channel<SystemMessage> = Channel(Channel.RENDEZVOUS)
    val messages: Flow<SystemMessage>
        get() = messageChannel.consumeAsFlow()

    fun send(message: String) {
        messageChannel.sendBlocking(SystemMessage(message))
    }

    fun send(message: SystemMessage) {
        messageChannel.sendBlocking(message)
    }
}
package com.rmyhal.nestegg.ui

class Command(private val action: () -> Unit) {
    fun perform() {
        action()
    }
}
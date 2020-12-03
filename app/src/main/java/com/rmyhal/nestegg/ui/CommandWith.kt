package com.rmyhal.nestegg.ui

class CommandWith<T>(private val action: (T) -> Unit) {
    fun perform(with: T) {
        action(with)
    }
}
package com.rmyhal.nestegg.ui

data class Field<T>(val value: T, val update: CommandWith<T> = CommandWith {  })
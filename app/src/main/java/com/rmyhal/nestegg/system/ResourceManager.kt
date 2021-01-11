package com.rmyhal.nestegg.system

import android.content.Context

class ResourceManager(private val context: Context) {

    fun getString(id: Int) = context.getString(id)

    fun getString(id: Int, vararg formatArgs: Any) = String.format(context.getString(id, *formatArgs))
}
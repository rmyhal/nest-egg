package com.rmyhal.nestegg.system

import android.content.Context
import androidx.annotation.StringRes

class ResourceManager(private val context: Context) {

    fun getString(@StringRes id: Int) = context.getString(id)

    fun getString(@StringRes id: Int, vararg formatArgs: Any) = String.format(context.getString(id, *formatArgs))

    fun getDrawableResByName(name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }
}
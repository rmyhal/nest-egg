package com.rmyhal.shared.cache

import com.squareup.sqldelight.db.SqlDriver

const val dbName: String = "app.db"

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

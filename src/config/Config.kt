package com.batcuevasoft.config

class Config(
    val host: String,
    val port: Int,
    val databaseHost: String,
    val databasePort: String
) {

    companion object {
        val databaseName: String = "ktoreasydb"
        val databaseUser: String = "ktoreasyuser"
        val databasePassword: String = "ktoreasypassword"
    }
}
package com.batcuevasoft.config

class Config(
    val host: String,
    val port: Int,
    val databaseHost: String,
    val databasePort: String
) {

    companion object {
        const val DATABASENAME: String = "ktoreasydb"
        const val DATABASEUSER: String = "ktoreasyuser"
        const val DATABASEPASSWORD: String = "ktoreasypassword"
    }
}
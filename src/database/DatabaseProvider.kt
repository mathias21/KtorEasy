package com.batcuevasoft.database

import com.batcuevasoft.config.Config
import com.batcuevasoft.database.dao.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext

@OptIn(ObsoleteCoroutinesApi::class)
class DatabaseProvider : DatabaseProviderContract, KoinComponent {

    private val config by inject<Config>()
    private val dispatcher: CoroutineContext

    init {
        dispatcher = newFixedThreadPoolContext(5, "database-pool")
    }

    override fun init() {
        Database.connect(hikari(config))
        transaction {
            create(Users)
        }
    }

    private fun hikari(mainConfig: Config): HikariDataSource {
        HikariConfig().run {
            driverClassName = "com.mysql.jdbc.Driver"
            jdbcUrl = "jdbc:mysql://${mainConfig.databaseHost}:${mainConfig.databasePort}/${Config.DATABASENAME}"
            username = Config.DATABASEUSER
            password = Config.DATABASEPASSWORD
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
            return HikariDataSource(this)
        }
    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(dispatcher) {
        transaction { block() }
    }

}

interface DatabaseProviderContract {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
}
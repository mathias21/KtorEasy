package com.batcuevasoft.dao

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach

abstract class BaseDaoTest {

    @BeforeEach
    open fun setup() {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
    }

    fun withTables(vararg tables: Table, test: Transaction.() -> Unit)
    {
        transaction {
            SchemaUtils.create(*tables)
            try {
                test()
                commit()
            } finally {
                SchemaUtils.drop(*tables)
                commit()
            }
        }
    }
}
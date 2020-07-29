package com.batcuevasoft.controllers

import com.batcuevasoft.database.DatabaseProviderContract
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

abstract class BaseControllerTest {

    private val databaseProvider: DatabaseProviderContract = mockk()

    init {
        stopKoin()
    }

    open fun before() {
        clearMocks(databaseProvider)
        coEvery { databaseProvider.dbQuery(any<() -> Any>()) } coAnswers {
            firstArg<() -> Any>().invoke()
        }
    }

    fun startInjection(module: Module) {
        startKoin {
            modules(
                module,
                module {
                    single(override = true) { databaseProvider }
                }
            )
        }
    }
}
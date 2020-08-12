package com.batcuevasoft.api

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

abstract class BaseApiTest {

    init {
        stopKoin()
    }

    fun startInjection(module: Module) {
        startKoin {
            modules(
                module
            )
        }
    }
}
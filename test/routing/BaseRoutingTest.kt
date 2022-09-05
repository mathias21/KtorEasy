package com.batcuevasoft.routing

import io.ktor.client.HttpClient
import io.ktor.serialization.gson.gson
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.util.logging.KtorSimpleLogger
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

abstract class BaseRoutingTest {

    abstract val koinModules: Module
    abstract val moduleList: ApplicationTestBuilder.() -> Unit

    @BeforeEach
    fun shutdownKoin() {
        stopKoin()
    }

    fun <R> withBaseTestApplication(test: suspend ApplicationTestBuilder.() -> R) {
        testApplication {
            environment {
                config = MapApplicationConfig("ktor.deployment.environment" to "test")
                log = KtorSimpleLogger("ktor.test")
                developmentMode = true
            }

            koinModules.let {
                install(Koin) {
                    modules(it)
                }
            }
            install(ContentNegotiation) { gson { } }

            moduleList()

            test()
        }
    }
}

val ApplicationTestBuilder.customClient: HttpClient
    get() = createClient {
        install(ClientContentNegotiation) { gson { } }
    }
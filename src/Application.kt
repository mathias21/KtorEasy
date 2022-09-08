package com.batcuevasoft

import com.auth0.jwt.interfaces.JWTVerifier
import com.batcuevasoft.api.injection.ApiInjection
import com.batcuevasoft.config.Config
import com.batcuevasoft.database.DatabaseProvider
import com.batcuevasoft.database.DatabaseProviderContract
import com.batcuevasoft.database.injection.DaoInjection
import com.batcuevasoft.modules.auth.JwtConfig
import com.batcuevasoft.modules.auth.TokenProvider
import com.batcuevasoft.modules.injection.ModulesInjection
import com.batcuevasoft.util.PasswordManager
import com.batcuevasoft.util.PasswordManagerContract
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main() {

    val environment = System.getenv()["ENVIRONMENT"] ?: handleDefaultEnvironment()
    val config = extractConfig(environment, HoconApplicationConfig(ConfigFactory.load()))

    embeddedServer(Netty, port = config.port) {
        println("Starting instance in ${config.host}:${config.port}")
        module {
            install(Koin) {
                modules(
                    module {
                        single { config }
                        single<DatabaseProviderContract> { DatabaseProvider() }
                        single<JWTVerifier> { JwtConfig.verifier }
                        single<PasswordManagerContract> { PasswordManager }
                        single<TokenProvider> { JwtConfig }
                    },
                    ApiInjection.koinBeans,
                    ModulesInjection.koinBeans,
                    DaoInjection.koinBeans
                )
            }
            main()
        }
    }.start(wait = true)
}

fun handleDefaultEnvironment(): String {
    println("Falling back to default environment 'dev'")
    return "dev"
}

fun Application.main() {
    module()
}

fun extractConfig(environment: String, hoconConfig: HoconApplicationConfig): Config {
    val hoconEnvironment = hoconConfig.config("ktor.deployment.$environment")
    return Config(
        hoconEnvironment.property("host").getString(),
        Integer.parseInt(hoconEnvironment.property("port").getString()),
        hoconEnvironment.property("databaseHost").getString(),
        hoconEnvironment.property("databasePort").getString()
    )
}


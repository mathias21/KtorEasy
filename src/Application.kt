package com.batcuevasoft

import com.auth0.jwt.interfaces.JWTVerifier
import com.batcuevasoft.api.injection.ApiInjection
import com.batcuevasoft.config.Config
import com.batcuevasoft.database.DatabaseProvider
import com.batcuevasoft.database.DatabaseProviderContract
import com.batcuevasoft.modules.auth.JwtConfig
import com.batcuevasoft.modules.injection.ModulesInjection
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.config.HoconApplicationConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun main(args: Array<String>) {

    val environment = System.getenv()["ENVIRONMENT"] ?: handleDefaultEnvironment()
    val config = extractConfig(environment, HoconApplicationConfig(ConfigFactory.load()))

    embeddedServer(Netty, port = config.port) {
        println("Starting instance in ${config.host}:${config.port}")
        module {
            install(Koin) {
                modules(
                    module {
                        single<DatabaseProviderContract> { DatabaseProvider() }
                        single<JWTVerifier> { JwtConfig.verifier }
                        single { config }
                    },
                    ApiInjection.koinBeans,
                    ModulesInjection.koinBeans
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

@KtorExperimentalAPI
fun extractConfig(environment: String, hoconConfig: HoconApplicationConfig): Config {
    val hoconEnvironment = hoconConfig.config("ktor.deployment.$environment")
    return Config(
        hoconEnvironment.property("host").getString(),
        Integer.parseInt(hoconEnvironment.property("port").getString()),
        hoconEnvironment.property("databaseHost").getString(),
        hoconEnvironment.property("databasePort").getString()
    )
}


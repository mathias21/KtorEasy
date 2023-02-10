package com.batcuevasoft.modules.metrics


import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.get

fun Route.metrics() {

    val registry = get<PrometheusMeterRegistry>()

    get("/metrics") {
        call.respondText {
            registry.scrape()
        }
    }
}
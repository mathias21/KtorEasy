package com.batcuevasoft.metrics

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.get

fun Routing.metrics() {

    val registry = get<PrometheusMeterRegistry>()

    get("/metrics") {
        call.respondText {
            registry.scrape()
        }
    }
}
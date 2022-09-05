package com.batcuevasoft.statuspages

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respond

fun StatusPagesConfig.generalStatusPages() {
    exception<MissingArgumentException> { call, cause ->
        call.respond(HttpStatusCode.BadRequest, cause.message)
    }
}

data class MissingArgumentException(override val message: String = "Missing argument") : Exception()
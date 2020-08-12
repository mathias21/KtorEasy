package com.batcuevasoft.statuspages

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.generalStatusPages() {
    exception<MissingArgumentException> { cause ->
        call.respond(HttpStatusCode.BadRequest, cause.message)
    }
}

data class MissingArgumentException(override val message: String = "Missing argument") : Exception()
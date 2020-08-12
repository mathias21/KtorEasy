package com.batcuevasoft.statuspages

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.userStatusPages() {
    exception<InvalidUserException> { cause ->
        call.respond(HttpStatusCode.BadRequest, cause.localizedMessage)
    }
}

data class InvalidUserException(override val message: String = "Invalid user") : Exception()
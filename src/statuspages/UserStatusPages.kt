package com.batcuevasoft.statuspages

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respond

fun StatusPagesConfig.userStatusPages() {
    exception<InvalidUserException> { call, cause ->
        call.respond(HttpStatusCode.BadRequest, cause.localizedMessage)
    }
}

data class InvalidUserException(override val message: String = "Invalid user") : Exception()
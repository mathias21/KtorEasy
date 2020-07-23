package com.batcuevasoft.statuspages

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText

fun StatusPages.Configuration.authStatusPages() {
    exception<AuthenticationException> { cause ->
        call.respondText(cause.message, ContentType.Text.Plain, status = HttpStatusCode.Unauthorized)
    }
    exception<AuthorizationException> { cause ->
        call.respondText(cause.message, ContentType.Text.Plain, status = HttpStatusCode.Forbidden)
    }
}

data class AuthenticationException(override val message: String = "Authentication failed") : Exception()
data class AuthorizationException(override val message: String = "You are not authorised to use this service") : Exception()

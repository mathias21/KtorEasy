package com.batcuevasoft

import com.auth0.jwt.interfaces.JWTVerifier
import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.database.DatabaseProviderContract
import com.batcuevasoft.model.User
import com.batcuevasoft.modules.auth.authenticationModule
import com.batcuevasoft.modules.registration.registrationModule
import com.batcuevasoft.modules.user.userModule
import com.batcuevasoft.statuspages.authStatusPages
import com.batcuevasoft.statuspages.generalStatusPages
import com.batcuevasoft.statuspages.userStatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.PipelineContext
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun Application.module() {

    val userApi by inject<UserApi>()
    val databaseProvider by inject<DatabaseProviderContract>()
    val jwtVerifier by inject<JWTVerifier>()
    //Init database here
    databaseProvider.init()

    install(CallLogging) {
        level = Level.DEBUG
    }
    install(ContentNegotiation) { gson { } }
    install(StatusPages) {
        generalStatusPages()
        userStatusPages()
        authStatusPages()
        exception<UnknownError> { call, _ ->
            call.respondText(
                "Internal server error",
                ContentType.Text.Plain,
                status = HttpStatusCode.InternalServerError
            )
        }
        exception<IllegalArgumentException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    install(Authentication) {
        authenticationModule(userApi, databaseProvider, jwtVerifier)
    }

    install(Routing) {
        static("/static") {
            resources("static")
        }
        registrationModule()
        authenticate("jwt") {
            userModule()
        }
    }
}

val ApplicationCall.user get() = authentication.principal<User>()!!

suspend fun PipelineContext<Unit, ApplicationCall>.sendOk() {
    call.respond(HttpStatusCode.OK)
}
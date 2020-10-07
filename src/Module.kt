package com.batcuevasoft

import com.auth0.jwt.interfaces.JWTVerifier
import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.database.DatabaseProviderContract
import com.batcuevasoft.metrics.metrics
import com.batcuevasoft.model.User
import com.batcuevasoft.modules.auth.authenticationModule
import com.batcuevasoft.modules.registration.registrationModule
import com.batcuevasoft.modules.user.userModule
import com.batcuevasoft.statuspages.authStatusPages
import com.batcuevasoft.statuspages.generalStatusPages
import com.batcuevasoft.statuspages.userStatusPages
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.pipeline.PipelineContext
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun Application.module() {

    val userApi by inject<UserApi>()
    val databaseProvider by inject<DatabaseProviderContract>()
    val jwtVerifier by inject<JWTVerifier>()
    //Init database here
    databaseProvider.init()

    install(MicrometerMetrics) {
        registry = get<PrometheusMeterRegistry>()
        meterBinders = listOf(
            ClassLoaderMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics(),
            JvmThreadMetrics(),
            FileDescriptorMetrics(),
            UptimeMetrics()
        )
    }
    install(CallLogging) {
        level = Level.DEBUG
    }
    install(ContentNegotiation) { gson { } }
    install(StatusPages) {
        generalStatusPages()
        userStatusPages()
        authStatusPages()
        exception<UnknownError> {
            call.respondText(
                "Internal server error",
                ContentType.Text.Plain,
                status = HttpStatusCode.InternalServerError
            )
        }
        exception<IllegalArgumentException> {
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
        // We add our metrics endpoint to route definition
        metrics()
    }
}

val ApplicationCall.user get() = authentication.principal<User>()!!

suspend fun PipelineContext<Unit, ApplicationCall>.sendOk() {
    call.respond(HttpStatusCode.OK)
}
package com.batcuevasoft

import com.auth0.jwt.interfaces.JWTVerifier
import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.database.DatabaseProviderContract
import com.batcuevasoft.modules.metrics.metrics
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
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.PipelineContext
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun Application.module() {

    val userApi by inject<UserApi>()
    val databaseProvider by inject<DatabaseProviderContract>()
    val jwtVerifier by inject<JWTVerifier>()
    val prometeusRegistry by inject<PrometheusMeterRegistry>()
    //Init database here
    databaseProvider.init()

    install(MicrometerMetrics) {
        registry = prometeusRegistry
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
        // We add our metrics endpoint to route definition
        metrics()
    }
}

val ApplicationCall.user get() = authentication.principal<User>()!!

suspend fun PipelineContext<Unit, ApplicationCall>.sendOk() {
    call.respond(HttpStatusCode.OK)
}
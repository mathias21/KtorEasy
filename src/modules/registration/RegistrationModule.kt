package com.batcuevasoft.modules.registration

import com.batcuevasoft.model.LoginCredentials
import com.batcuevasoft.model.PostUserBody
import com.batcuevasoft.model.RefreshBody
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Routing.registrationModule() {

    val unauthenticatedController by inject<RegistrationController>()

    post("user") {
        val postUser = call.receive<PostUserBody>()
        val user = unauthenticatedController.createUser(postUser)
        call.respond(user)
    }

    post("authenticate") {
        val credentials = call.receive<LoginCredentials>()
        val loginTokenResponse = unauthenticatedController.authenticate(credentials)
        call.respond(loginTokenResponse)
    }

    post("token") {
        val credentials = call.receive<RefreshBody>()
        val credentialsResponse = unauthenticatedController.refreshToken(credentials)
        call.respond(credentialsResponse)
    }
}
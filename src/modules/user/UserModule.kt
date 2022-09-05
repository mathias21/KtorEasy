package com.batcuevasoft.modules.user

import com.batcuevasoft.model.PutUserBody
import com.batcuevasoft.model.toResponseUser
import com.batcuevasoft.sendOk
import com.batcuevasoft.user
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Route.userModule() {

    val controller by inject<UserController>()

    get("me") {
        call.respond(call.user.toResponseUser()) // avoid sending
    }

    put("updateProfile") {
        val putUser = call.receive<PutUserBody>()
        val user = controller.updateProfile(call.user.id, putUser)
        call.respond(user.toResponseUser())
    }

    delete("user") {
        controller.removeUser(call.user.id)
        sendOk()
    }
}
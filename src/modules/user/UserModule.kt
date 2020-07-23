package com.batcuevasoft.modules.user

import com.batcuevasoft.model.PutUserBody
import com.batcuevasoft.model.toResponseUser
import com.batcuevasoft.sendOk
import com.batcuevasoft.user
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
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
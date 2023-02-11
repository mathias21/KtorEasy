package com.batcuevasoft.routing.instrumentation

import com.batcuevasoft.model.PostUserBody
import com.batcuevasoft.model.ResponseUser

object RegistrationControllerInstrumentation {

    fun givenAResponseUser(id: Int = 123): ResponseUser {
        return ResponseUser(
            id,
            name = "name",
            secondname = "secondname",
            username = "user@gmail.com"
        )
    }

    fun givenPostUserBody() = PostUserBody(
        name = "name",
        secondname = "secondname",
        username = "user@gmail.com",
        password = "password"
    )
}
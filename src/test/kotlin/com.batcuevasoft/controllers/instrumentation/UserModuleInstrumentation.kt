package com.batcuevasoft.controllers.instrumentation

import com.batcuevasoft.model.User

object UserModuleInstrumentation {

    fun givenAnUser(id: Int = 123): User {
        return User(
            id,
            name = "name",
            secondname = "secondname",
            username = "user@gmail.com",
            password = "mypass"
        )
    }
}
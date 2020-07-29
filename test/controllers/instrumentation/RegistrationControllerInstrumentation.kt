package com.batcuevasoft.controllers.instrumentation

import com.batcuevasoft.model.CredentialsResponse
import com.batcuevasoft.model.LoginCredentials
import com.batcuevasoft.model.PostUserBody
import com.batcuevasoft.model.RefreshBody

object RegistrationControllerInstrumentation {

    fun givenAValidPostUserBody() = PostUserBody(
        "name",
        "secondname",
        "username@domain.com",
        "password"
    )


    fun givenALoginCredentials() = LoginCredentials(
        "username",
        "pasword"
    )

    fun givenACredentialsResponse() = CredentialsResponse(
        "token",
        "refresh"
    )

    fun givenARefreshBody() = RefreshBody(
        "username",
        "refresh"
    )
}
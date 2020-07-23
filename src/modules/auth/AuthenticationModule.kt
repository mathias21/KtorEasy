package com.batcuevasoft.modules.auth

import com.auth0.jwt.interfaces.JWTVerifier
import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.database.DatabaseProviderContract
import io.ktor.application.ApplicationCall
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.jwt.jwt
import io.ktor.routing.Routing

fun Authentication.Configuration.authenticationModule(
    userApi: UserApi,
    databaseProvider: DatabaseProviderContract,
    tokenVerifier: JWTVerifier
) {
    /**
     * Setup the JWT authentication to be used in [Routing].
     * If the token is valid, the corresponding [User] is fetched from the database.
     * The [User] can then be accessed in each [ApplicationCall].
     */
    jwt("jwt") {

        verifier(tokenVerifier)
        realm = "ktor.io"
        validate {
            it.payload.getClaim("id").asInt()?.let { userId ->
                // do database query to find Principal subclass
                MyUser(userId)
            }
        }
    }
}

data class MyUser(val id: Int) : Principal
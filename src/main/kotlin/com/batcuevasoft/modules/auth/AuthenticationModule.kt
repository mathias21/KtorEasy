package com.batcuevasoft.modules.auth

import com.auth0.jwt.interfaces.JWTVerifier
import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.database.DatabaseProviderContract
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun AuthenticationConfig.authenticationModule(
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
                databaseProvider.dbQuery {
                    userApi.getUserById(userId)
                }
            }
        }
    }
}
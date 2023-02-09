package com.batcuevasoft.modules.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.batcuevasoft.model.CredentialsResponse
import com.batcuevasoft.model.User
import java.util.*

object JwtConfig : TokenProvider {

    private const val secret = "PlaceYourSecretHere"
    private const val issuer = "PlaceYourIssuerHere"
    private const val validityInMs: Long = 3600000L * 24L // 24h
    private const val refrehsValidityInMs: Long = 3600000L * 24L * 30L // 30 days
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    override fun verifyToken(token: String): Int? {
        return verifier.verify(token).claims["id"]?.asInt()
    }

    /**
     * Produce token and refresh token for this combination of User and Account
     */
    override fun createTokens(user: User) = CredentialsResponse(
        createToken(user, getTokenExpiration()),
        createToken(user, getTokenExpiration(refrehsValidityInMs))
    )

    private fun createToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("name", user.name)
        .withExpiresAt(expiration)
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getTokenExpiration(validity: Long = validityInMs) = Date(System.currentTimeMillis() + validity)
}

interface TokenProvider {
    fun createTokens(user: User): CredentialsResponse
    fun verifyToken(token: String): Int?
}
package com.batcuevasoft.modules.registration

import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.model.LoginCredentials
import com.batcuevasoft.model.LoginTokenResponse
import com.batcuevasoft.model.PostUserBody
import com.batcuevasoft.model.RefreshBody
import com.batcuevasoft.model.ResponseUser
import com.batcuevasoft.model.toResponseUser
import com.batcuevasoft.modules.BaseController
import com.batcuevasoft.modules.auth.TokenProvider
import com.batcuevasoft.statuspages.AuthenticationException
import com.batcuevasoft.statuspages.InvalidUserException
import com.batcuevasoft.util.PasswordManagerContract
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegistrationControllerImp : BaseController(), RegistrationController, KoinComponent {

    private val userApi by inject<UserApi>()
    private val passwordManager by inject<PasswordManagerContract>()
    private val tokenProvider by inject<TokenProvider>()

    override suspend fun createUser(postUser: PostUserBody): ResponseUser {
        val user = dbQuery {
            userApi.getUserByUsername(postUser.username)?.let {
                throw InvalidUserException("User is already taken")
            }
            userApi.createUser(postUser) ?: throw UnknownError("Internal server error")
        }
        return user.toResponseUser()
    }

    override suspend fun authenticate(credentials: LoginCredentials) = dbQuery {
        userApi.getUserByUsername(credentials.username)?.let { user ->
            if (passwordManager.validatePassword(credentials.password, user.password)) {
                val credentialsResponse = tokenProvider.createTokens(user)
                LoginTokenResponse(credentialsResponse)
            } else {
                throw AuthenticationException("Wrong credentials")
            }
        } ?: throw AuthenticationException("Wrong credentials")
    }

    override suspend fun refreshToken(credentials: RefreshBody) = dbQuery {
        tokenProvider.verifyToken(credentials.refreshToken)?.let {
            userApi.getUserById(it)?.let {
                val credentialsResponse = tokenProvider.createTokens(it)
                LoginTokenResponse(credentialsResponse)
            } ?: throw AuthenticationException("Wrong credentials")
        } ?: throw AuthenticationException("Wrong credentials")
    }
}

interface RegistrationController {
    suspend fun createUser(postUser: PostUserBody): ResponseUser
    suspend fun authenticate(credentials: LoginCredentials): LoginTokenResponse
    suspend fun refreshToken(credentials: RefreshBody): LoginTokenResponse
}
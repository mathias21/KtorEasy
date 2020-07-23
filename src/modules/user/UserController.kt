package com.batcuevasoft.modules.user

import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.model.PutUserBody
import com.batcuevasoft.model.User
import com.batcuevasoft.modules.BaseController
import com.batcuevasoft.statuspages.InvalidUserException
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserControllerImp : BaseController(), UserController, KoinComponent {

    private val userApi by inject<UserApi>()

    override suspend fun updateProfile(userId: Int, putUser: PutUserBody) = dbQuery {
        userApi.updateUserProfile(userId, putUser)?.let {
            it
        } ?: throw InvalidUserException()
    }

    override suspend fun removeUser(userId: Int) {
        dbQuery {
            try {
                userApi.removeUser(userId)
            } catch (e: Exception) {
                throw UnknownError("Internal server error")
            }
        }
    }

}

interface UserController {
    suspend fun updateProfile(userId: Int, putUser: PutUserBody): User
    suspend fun removeUser(userId: Int)
}
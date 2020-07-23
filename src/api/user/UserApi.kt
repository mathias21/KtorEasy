package com.batcuevasoft.api.user

import com.batcuevasoft.model.PostUserBody
import com.batcuevasoft.model.PutUserBody
import com.batcuevasoft.model.User

interface UserApi {
    fun getUserById(id: Int): User?
    fun getUserByUsername(username: String): User?
    fun updateUserProfile(userId: Int, putUserBody: PutUserBody): User?
    fun removeUser(userId: Int): Boolean
    fun createUser(postUser: PostUserBody): User?
}
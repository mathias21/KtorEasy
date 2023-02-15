package com.batcuevasoft.dao

import com.batcuevasoft.controllers.instrumentation.RegistrationControllerInstrumentation.givenAValidPostUserBody
import com.batcuevasoft.database.dao.Users
import com.batcuevasoft.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest : BaseDaoTest() {

    @Test
    fun `when creating user with correct information and user not taken, database is storing user`() {
        withTables(Users) {
            val validPostUser = givenAValidPostUserBody()
            val userId = Users.insertUser(validPostUser)
            userId?.let {
                val user = Users.getUserById(userId)
                assertThat(user).isEqualTo(
                    User(
                        userId,
                        validPostUser.name,
                        validPostUser.secondname,
                        validPostUser.username,
                        validPostUser.password
                    )
                )
            } ?: throw IllegalStateException("UserId cannot be null")
        }
    }
}
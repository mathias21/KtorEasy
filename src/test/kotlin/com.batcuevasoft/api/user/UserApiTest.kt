package com.batcuevasoft.api.user

import com.batcuevasoft.api.BaseApiTest
import com.batcuevasoft.controllers.instrumentation.UserModuleInstrumentation.givenAnUser
import com.batcuevasoft.database.dao.UserDao
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.dsl.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserApiTest : BaseApiTest() {

    private val userDao: UserDao = mockk()
    private val api: UserApi = UserApiImpl

    init {
        startInjection(
            module {
                single { userDao }
            }
        )
    }

    @BeforeEach
    fun before() {
        clearMocks(userDao)
    }

    @Test
    fun `when fetching user by id and user exists, we return user`() {
        val user = givenAnUser()
        every { userDao.getUserById(any()) } returns user

        val responseUser = api.getUserById(123)
        assertThat(user).isEqualTo(responseUser)
    }

    @Test
    fun `when fetching user by id and it does not exists, we return null`() {
        every { userDao.getUserById(any()) } returns null

        val responseUser = api.getUserById(123)
        assertThat(responseUser).isNull()
    }
}
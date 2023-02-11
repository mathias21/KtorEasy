package com.batcuevasoft.controllers.registration

import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.controllers.BaseControllerTest
import com.batcuevasoft.controllers.instrumentation.RegistrationControllerInstrumentation.givenAValidPostUserBody
import com.batcuevasoft.controllers.instrumentation.UserModuleInstrumentation.givenAnUser
import com.batcuevasoft.modules.registration.RegistrationController
import com.batcuevasoft.modules.registration.RegistrationControllerImp
import com.batcuevasoft.statuspages.InvalidUserException
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.dsl.module


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateUserTest : BaseControllerTest() {

    private val userApi: UserApi = mockk()
    private val controller: RegistrationController by lazy { RegistrationControllerImp() }
    private val userId = 11

    init {
        startInjection(
            module {
                single(override = true) { userApi }
            }
        )
    }

    @BeforeEach
    override fun before() {
        super.before()
        clearMocks(userApi)
    }

    @Test
    fun `when creating user with correct information and user not taken, we return a valid ResponseUser`() {
        val postUser = givenAValidPostUserBody()
        val createdUser = givenAnUser(userId)

        // Setup
        coEvery { userApi.getUserByUsername(any()) } returns null
        coEvery { userApi.createUser(any()) } returns createdUser

        runBlocking {

            // call to subject under test
            val responseUser = controller.createUser(postUser)


            // Asertion
            assertThat(responseUser.id).isEqualTo(userId)
            assertThat(responseUser.name).isEqualTo(postUser.name)
            assertThat(responseUser.secondname).isEqualTo(postUser.secondname)
            assertThat(responseUser.username).isEqualTo(createdUser.username)
        }

    }

    @Test
    fun `when creating user with username already taken, we throw exception`() {
        val postUser = givenAValidPostUserBody()
        val createdUser = givenAnUser(userId)

        // COnfig
        coEvery { userApi.getUserByUsername(any()) } returns createdUser

        // Asercion                                     // Llamada a la funcion
        assertThrows(InvalidUserException::class.java) {
            runBlocking { controller.createUser(postUser) }
        }
    }

    @Test
    fun `when creating user and database returns error, we throw exception`() {
        val postUser = givenAValidPostUserBody()

        coEvery { userApi.getUserByUsername(any()) } returns null
        coEvery { userApi.createUser(any()) } returns null

        assertThrows(UnknownError::class.java) { runBlocking { controller.createUser(postUser) } }
    }

}
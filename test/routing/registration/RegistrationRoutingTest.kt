package com.batcuevasoft.routing.registration

import com.batcuevasoft.model.ResponseUser
import com.batcuevasoft.modules.registration.RegistrationController
import com.batcuevasoft.modules.registration.registrationModule
import com.batcuevasoft.routing.BaseRoutingTest
import com.batcuevasoft.routing.customClient
import com.batcuevasoft.routing.instrumentation.RegistrationControllerInstrumentation.givenAResponseUser
import com.batcuevasoft.routing.instrumentation.RegistrationControllerInstrumentation.givenPostUserBody
import com.batcuevasoft.statuspages.InvalidUserException
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.Routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module

class RegistrationRoutingTest : BaseRoutingTest() {

    private val registrationController: RegistrationController = mockk()

    override val koinModules: Module = module {
        single { registrationController }
    }

    override val moduleList: ApplicationTestBuilder.() -> Unit = {
        install(Routing) {
            registrationModule()
        }
    }

    @BeforeEach
    fun clearMocks() {
        clearMocks(registrationController)
    }

    @Test
    fun `when creating user with successful insertion, we return response user body`() =
        withBaseTestApplication {
            val userId = 11
            val responseUser = givenAResponseUser(userId)
            coEvery { registrationController.createUser(any()) } returns responseUser

            val response = customClient.post("/user") {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(givenPostUserBody())
            }
            assertThat(HttpStatusCode.OK).isEqualTo(response.status)
            val responseBody = response.body<ResponseUser>()
            assertThat(responseUser).isEqualTo(responseBody)
        }

    @Test
    fun `when creating user already created, we return 400 error`() =
        withBaseTestApplication {
            coEvery { registrationController.createUser(any()) } throws InvalidUserException("User is already taken")

            val exception = assertThrows<InvalidUserException> {
                customClient.post("/user") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(givenPostUserBody())
                }
            }
            assertThat(exception.message).isEqualTo("User is already taken")
        }
}

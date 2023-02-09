package com.batcuevasoft.modules.injection

import com.batcuevasoft.modules.registration.RegistrationController
import com.batcuevasoft.modules.registration.RegistrationControllerImp
import com.batcuevasoft.modules.user.UserController
import com.batcuevasoft.modules.user.UserControllerImp
import org.koin.dsl.module

object ModulesInjection {
    val koinBeans = module {
        single<RegistrationController> { RegistrationControllerImp() }
        single<UserController> { UserControllerImp() }
    }
}
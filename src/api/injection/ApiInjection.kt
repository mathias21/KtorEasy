package com.batcuevasoft.api.injection

import com.batcuevasoft.api.user.UserApi
import com.batcuevasoft.api.user.UserApiImpl
import org.koin.dsl.module

object ApiInjection {
    val koinBeans = module {
        single<UserApi> { UserApiImpl }
    }
}
package com.batcuevasoft.database.injection

import com.batcuevasoft.database.dao.Users
import org.koin.dsl.module

object DaoInjection {
    val koinBeans = module {
        single { Users }
    }
}
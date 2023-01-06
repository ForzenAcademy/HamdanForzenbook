package com.hamdan.forzenbook.data.repository

import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.network.LoginService

class LoginRepositoryImpl(
    private val dao: LoginDao,
    private val service: LoginService
) : LoginRepository {
    override suspend fun requestValidation(email: String): Boolean {
        return service.requestValidation(email).isSuccessful
    }

    // TODO Implement Getting the Token
    override suspend fun getToken(email: String, code: String): LoginData? {
        TODO("Not yet implemented")
    }
}

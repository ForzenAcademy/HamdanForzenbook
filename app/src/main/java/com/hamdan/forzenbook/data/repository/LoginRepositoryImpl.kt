package com.hamdan.forzenbook.data.repository

import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.network.LoginResponse
import com.hamdan.forzenbook.data.network.LoginService

class LoginRepositoryImpl(
    // TODO implement the dao FA-84
    private val dao: LoginDao,
    private val service: LoginService
) : LoginRepository {
    override suspend fun requestValidation(email: String) {
        service.requestValidation(email).isSuccessful
    }

    override suspend fun getToken(email: String, code: String): LoginResponse {
        val response = service.getToken(email, code)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Failed to get Token")
        } else throw Exception("Failed to get Token")
    }
}

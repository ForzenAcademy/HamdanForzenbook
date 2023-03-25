package com.hamdan.forzenbook.login.core.data.repository

import com.hamdan.forzenbook.login.core.data.network.LoginService

class LoginRepositoryImpl(
    private val service: LoginService
) : LoginRepository {
    override suspend fun requestValidation(email: String) {
        service.requestValidation(email)
    }

    override suspend fun getToken(email: String?, code: String?): String? {
        return email?.let { code?.let { getTokenFromNetwork(email, code) } }
    }

    private suspend fun getTokenFromNetwork(email: String, code: String): String {
        val response = service.getToken(email, code)
        if (response.isSuccessful) {
            val body = response.body() ?: throw FailTokenRetrievalException("Failed to get Token")
            body.token ?: throw NullTokenException("null token")
            return body.token
        } else throw FailTokenRetrievalException("Failed to get Token")
    }
}

class NullTokenException(message: String) : Exception(message)
class FailTokenRetrievalException(message: String) : Exception(message)

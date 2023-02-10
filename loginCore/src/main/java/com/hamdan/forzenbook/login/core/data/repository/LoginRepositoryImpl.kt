package com.hamdan.forzenbook.login.core.data.repository

import com.hamdan.forzenbook.login.core.data.database.LoginDao
import com.hamdan.forzenbook.login.core.data.database.LoginEntity
import com.hamdan.forzenbook.login.core.data.network.LoginService

class LoginRepositoryImpl(
    private val dao: LoginDao,
    private val service: LoginService
) : LoginRepository {
    override suspend fun requestValidation(email: String) {
        service.requestValidation(email)
    }

    override suspend fun getToken(email: String?, code: String?): User {
        return if (email == null && code == null) {
            getTokenFromDatabase()
        } else return email?.let { code?.let { getTokenFromNetwork(email, code) } }
            ?: User.NotLoggedIn
    }

    private suspend fun getTokenFromDatabase(): User {
        // we catch the exception up the chain if there is an issue
        return if (dao.getToken() == null) User.NotLoggedIn else User.LoggedIn
    }

    private suspend fun getTokenFromNetwork(email: String, code: String): User {
        val response = service.getToken(email, code)
        if (response.isSuccessful) {
            val body = response.body() ?: throw FailTokenRetrievalException("Failed to get Token")
            body.token ?: throw NullTokenException("null token")
            dao.insert(LoginEntity(body.token, email))
            return User.LoggedIn
        } else throw FailTokenRetrievalException("Failed to get Token")
    }
}

class NullTokenException(message: String) : Exception(message)
class FailTokenRetrievalException(message: String) : Exception(message)

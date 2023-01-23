package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.data.repository.NullTokenException
import com.hamdan.forzenbook.data.repository.User

class LoginUseCaseGetCredentialsFromNetworkImpl(
    val repository: LoginRepository
) : LoginUseCaseGetCredentialsFromNetwork {
    override suspend fun invoke(email: String, code: String) {
        val userState = repository.getToken(email, code)
        if (userState is User.LoggedIn) {
            // TODO logged in path when homepage implemented
        } else throw(NullTokenException("null token"))
    }
}

package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.data.repository.NullTokenException
import com.hamdan.forzenbook.data.repository.User

class LoginUseCaseGetStoredCredentialsImpl(
    val repository: LoginRepository
) : LoginUseCaseGetStoredCredentials {
    override suspend operator fun invoke() {
        val userState = repository.getToken()
        if (userState is User.LoggedIn) {
            // TODO logged in path when homepage implemented
        } else throw(NullTokenException("null token"))
    }
}

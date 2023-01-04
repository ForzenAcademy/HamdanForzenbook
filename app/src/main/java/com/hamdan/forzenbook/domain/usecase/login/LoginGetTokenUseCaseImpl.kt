package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginData
import com.hamdan.forzenbook.data.repository.LoginRepository

class LoginGetTokenUseCaseImpl(
    val repository: LoginRepository
) : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, password: String): LoginData? {
        return repository.getToken(email, password)
    }
}
package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.login.core.data.repository.LoginRepository
import com.hamdan.forzenbook.login.core.data.repository.NullTokenException

class LoginGetStoredCredentialsUseCaseImpl(
    val repository: LoginRepository
) : LoginGetStoredCredentialsUseCase {
    override suspend operator fun invoke(): String {
        val token = repository.getToken()
        return token ?: throw (NullTokenException("null token"))
    }
}

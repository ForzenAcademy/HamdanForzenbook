package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.login.core.data.repository.LoginRepository
import com.hamdan.forzenbook.login.core.data.repository.NullTokenException

class LoginGetCredentialsFromNetworkUseCaseImpl(
    val repository: LoginRepository
) : LoginGetCredentialsFromNetworkUseCase {
    override suspend fun invoke(email: String, code: String): String {
        val token = repository.getToken(email, code)
        return token ?: throw (NullTokenException("null token"))
    }
}

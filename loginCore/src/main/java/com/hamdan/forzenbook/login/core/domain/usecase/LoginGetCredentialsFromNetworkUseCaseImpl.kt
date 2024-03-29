package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.login.core.data.repository.LoginRepository

class LoginGetCredentialsFromNetworkUseCaseImpl(
    val repository: LoginRepository
) : LoginGetCredentialsFromNetworkUseCase {
    override suspend fun invoke(email: String, code: String) {
        repository.getToken(email, code)
    }
}

package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.core.NullTokenException
import com.hamdan.forzenbook.login.core.data.repository.LoginRepository

class LoginGetStoredCredentialsUseCaseImpl(
    val repository: LoginRepository
) : LoginGetStoredCredentialsUseCase {
    override suspend operator fun invoke() {
        repository.getToken()
    }
}

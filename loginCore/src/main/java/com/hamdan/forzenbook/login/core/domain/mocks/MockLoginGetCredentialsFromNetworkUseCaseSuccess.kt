package com.hamdan.forzenbook.login.core.domain.mocks

import com.hamdan.forzenbook.login.core.data.repository.LoginRepository
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase

class MockLoginGetCredentialsFromNetworkUseCaseSuccess(
    val repository: LoginRepository
) : LoginGetCredentialsFromNetworkUseCase {
    override suspend fun invoke(email: String, code: String): String {
        return "itsatoken"
    }
}

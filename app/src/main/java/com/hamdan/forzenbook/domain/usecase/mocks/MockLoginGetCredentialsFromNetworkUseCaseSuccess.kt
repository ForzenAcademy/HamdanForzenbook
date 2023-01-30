package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.domain.usecase.login.LoginGetCredentialsFromNetworkUseCase

class MockLoginGetCredentialsFromNetworkUseCaseSuccess(
    val repository: LoginRepository
) : LoginGetCredentialsFromNetworkUseCase {
    override suspend fun invoke(email: String, code: String) {
    }
}

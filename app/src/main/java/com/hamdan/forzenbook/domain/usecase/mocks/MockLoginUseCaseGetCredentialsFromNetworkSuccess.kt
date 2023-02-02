package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseGetCredentialsFromNetwork

class MockLoginUseCaseGetCredentialsFromNetworkSuccess(
    val repository: LoginRepository
) : LoginUseCaseGetCredentialsFromNetwork {
    override suspend fun invoke(email: String, code: String) {
    }
}

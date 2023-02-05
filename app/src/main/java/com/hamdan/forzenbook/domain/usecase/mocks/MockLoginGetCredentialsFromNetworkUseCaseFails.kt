package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.domain.usecase.login.LoginGetCredentialsFromNetworkUseCase

class MockLoginGetCredentialsFromNetworkUseCaseFails :
    LoginGetCredentialsFromNetworkUseCase {
    override suspend fun invoke(email: String, code: String) {
        throw RuntimeException("There was an issue getting the token!")
    }
}

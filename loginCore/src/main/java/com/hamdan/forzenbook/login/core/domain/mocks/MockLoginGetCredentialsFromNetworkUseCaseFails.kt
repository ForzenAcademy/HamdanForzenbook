package com.hamdan.forzenbook.login.core.domain.mocks

import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase

class MockLoginGetCredentialsFromNetworkUseCaseFails :
    LoginGetCredentialsFromNetworkUseCase {
    override suspend fun invoke(email: String, code: String) {
        throw RuntimeException("There was an issue getting the token!")
    }
}

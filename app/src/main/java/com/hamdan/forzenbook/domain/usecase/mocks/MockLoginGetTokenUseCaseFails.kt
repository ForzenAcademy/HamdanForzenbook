package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.data.repository.LoginData
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCase

class MockLoginGetTokenUseCaseFails : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, code: String): LoginData? {
        throw RuntimeException("There was an issue getting the token!")
    }
}

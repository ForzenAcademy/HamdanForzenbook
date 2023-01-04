package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.data.repository.LoginData
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCase

class MockLoginGetTokenUseCaseFails : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, password: String): LoginData? {
        throw RuntimeException("Something went wrong trying to login")
    }
}
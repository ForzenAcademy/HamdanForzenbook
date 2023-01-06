package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.data.repository.LoginData
import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCase

class MockLoginGetTokenUseCaseSuccess(
    val repository: LoginRepository
) : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, code: String): LoginData? {
        return LoginData("it worked")
    }
}

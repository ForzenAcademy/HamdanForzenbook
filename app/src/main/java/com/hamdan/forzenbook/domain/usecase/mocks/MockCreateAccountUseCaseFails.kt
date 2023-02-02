package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.domain.usecase.login.CreateAccountResult
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountUseCase

class MockCreateAccountUseCaseFails : CreateAccountUseCase {
    override suspend fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): CreateAccountResult {
        throw RuntimeException("Something went wrong requesting Account creation")
    }
}
